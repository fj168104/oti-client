package com.mr.sac.oti.bean;

import com.mr.framework.core.clone.Cloneable;
import com.mr.framework.core.util.StrUtil;
import com.mr.framework.db.DbUtil;
import com.mr.framework.db.Entity;
import com.mr.framework.db.handler.EntityListHandler;
import com.mr.framework.db.sql.SqlExecutor;
import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.DataType;
import com.mr.sac.oti.OTIContainer;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by feng on 18-5-6
 */
@Data
public class Field implements Node, Cloneable {
	private static final String CONSTANT_BEGIN = "#{";
	private static final String TABLE_BEGIN = "${";
	private static final String REPLACE_END = "}";

	private static Log log = LogFactory.get();

	private String fieldTag;
	private String defaultValue = "";
	private String description;
	//string int double object array
	private String dataType;

	private int length;
	private int decimalLength;
	private boolean isRequire;//是否必输
	private String tableField;

	private Object value;
	//报文中的值，用做日志记录
	private String parsedValue;
	//对field的值进行映射,可选属性, 预留字段
	private String replaceSQL = "";
	//tableSql替换参数后的sqlf
	private String selectSQL;

	//Message模板,object 或者 array时使用
	private Message messageTemplete;

	private List<Message> arrayMessage = new ArrayList<>();
	private Message objectMessage;

	private DataSource dataSource = OTIContainer.getDataSource();

	/**
	 * 基本类型string int double设置方法：
	 * 1：通过 <fieldTag, value> 直接赋值
	 * 2：通过参数#{param}替换来设置值
	 * <p>
	 * object 设置方法：
	 * 1：通过 <fieldTag, value> 直接赋值
	 * 2：通过参数#{param}替换来设置包含的Field的值
	 * <p>
	 * array 设置方法：
	 * 1：通过 <fieldTag, value> 直接赋值
	 * 2：通过参数#{param}替换来设置包含的Field的值
	 * 3: 通过tableField #{column}来替数据库匹配字段的值
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public void fillValue(Map<String, Object> parameters) throws Exception {
		if(Objects.isNull(parameters)) parameters = new LinkedHashMap<>();
		if (!Objects.isNull(parameters.get(fieldTag))) {
			if (parameters.get(fieldTag) instanceof List) {
				arrayMessage = (List<Message>) parameters.get(fieldTag);
			} else if (parameters.get(fieldTag) instanceof Message) {
				objectMessage = (Message) parameters.get(fieldTag);
			} else {
				value = parameters.get(fieldTag);
			}
			return;
		}

		switch (DataType.getCodeByName(dataType)) {
			case 1:
				value = replaceConstants(parameters, defaultValue);
				break;
			case 2:
				value = Integer.parseInt(replaceConstants(parameters, defaultValue));
				break;
			case 3:
				value = Double.parseDouble(replaceConstants(parameters, defaultValue));
				break;
			case 4:
				objectMessage = messageTemplete.clone();
				objectMessage.fillValue(parameters);
				break;
			case 5:
				replaceTableFields(parameters);
				break;
			default:
				break;
		}
	}

	private String replaceConstants(Map<String, Object> parameters, String replaceValue) {
		if (isTableReplace(replaceValue)) return replaceValue;
		if (!isConstantReplace(replaceValue)) return replaceValue;
		String tValue = replaceValue;
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			String key = CONSTANT_BEGIN + entry.getKey() + REPLACE_END;
			tValue = tValue.replace(key, String.valueOf(entry.getValue()));
		}
		return tValue;
	}

	/**
	 * 是否需要#{}来替换处理
	 */
	private boolean isConstantReplace(String constant) {
		if (StrUtil.isBlank(constant)) return false;
		return constant.startsWith(CONSTANT_BEGIN) && constant.endsWith(REPLACE_END);

	}

	/**
	 * 是否由${}来替换处理的
	 */
	private boolean isTableReplace(String colume) {
		if (StrUtil.isBlank(colume)) return false;
		return colume.startsWith(TABLE_BEGIN) && colume.endsWith(REPLACE_END);

	}

	public Field clone() {
		Field cloneField = new Field();

		cloneField.fieldTag = fieldTag;
		cloneField.defaultValue = defaultValue;
		cloneField.description = description;
		cloneField.dataType = dataType;
		cloneField.length = length;
		cloneField.decimalLength = decimalLength;
		cloneField.value = value;
		cloneField.isRequire = isRequire;

		//预留使用
		cloneField.parsedValue = parsedValue;
		cloneField.replaceSQL = replaceSQL;

		cloneField.tableField = tableField;

		cloneField.messageTemplete = messageTemplete.clone();

		return cloneField;
	}

	private void replaceTableFields(Map<String, Object> parameters) throws Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			selectSQL = replaceConstants(parameters, tableField);
			log.debug("FieldTag={}, selectSQL={}", fieldTag, selectSQL);
			if (StrUtil.isBlank(selectSQL)) return;
			List<Entity> entityList = SqlExecutor.query(conn, selectSQL, new EntityListHandler());
			log.info("{}", entityList);
			for(Entity entity : entityList) {
				Message tableMessage = messageTemplete.clone();
				for (Field field : tableMessage.getFieldMap().values()) {
					field.fillValue(parameters);
					if (!Objects.isNull(field.value) && isTableReplace(String.valueOf(field.value))) {
						String columnName = String.valueOf(field.value).replace(TABLE_BEGIN, "")
								.replace(REPLACE_END, "");
						if (dataType.equals(DataType.STRING)) {
							field.value = entity.getStr(columnName);
						} else if (dataType.equals(DataType.INT)) {
							field.value = entity.getInt(columnName);
						} else if (dataType.equals(DataType.DOUBLE)) {
							field.value = entity.getDouble(columnName);
						}
					}
				}
				arrayMessage.add(tableMessage);
			}
		} catch (SQLException e) {
			log.error("SQL error!");
		} finally {
			DbUtil.close(conn);
		}
	}

}
