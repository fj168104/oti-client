package com.mr.sac.oti.bean;

import com.mr.framework.core.clone.Cloneable;
import com.mr.framework.core.util.StrUtil;
import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.Dbable;
import com.mr.sac.oti.Node;
import com.mr.sac.oti.pack.Parser;
import lombok.Data;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-5-6
 */
@Data
public class Field implements Node, Cloneable, Dbable {
	private static final String CONSTANT_BEGIN = "#{";
	private static final String TABLE_BEGIN = "${";
	private static final String REPLACE_END = "}";

	Log log = LogFactory.get();

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

	@Setter
	private DataSource dataSource;

	public void pack(Parser parser) {
		parser.packField(this);
	}

	public void unpack(String receivedData, Parser parser) {
		parser.unpackMessage(receivedData);
	}

	public void fillValue(Map<String, Object> parameters) throws Exception {
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

		switch (FieldDataType.getCodeByName(fieldTag)) {
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
				selectSQL = replaceConstants(parameters, tableField);
				log.debug("FieldTag={}, selectSQL={}", fieldTag, selectSQL);
				if (StrUtil.isBlank(selectSQL)) return;
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(selectSQL);
				while (rs.next()) {
					Message tableMessage = messageTemplete.clone();
					for (Field field : tableMessage.getFields()) {
						field.fillValue(parameters);
						if (!Objects.isNull(field.value) && isTableReplace(String.valueOf(field.value))) {
							String columnName = String.valueOf(field.value).replace(TABLE_BEGIN, "")
									.replace(REPLACE_END, "");
							if (dataType.equals(FieldDataType.FIELD_DATATYPE_STRING)) {
								field.value = rs.getString(columnName);
							} else if (dataType.equals(FieldDataType.FIELD_DATATYPE_INT)) {
								field.value = rs.getInt(columnName);
							} else if (dataType.equals(FieldDataType.FIELD_DATATYPE_DOUBLE)) {
								field.value = rs.getDouble(columnName);
							}
						}
					}
					arrayMessage.add(tableMessage);
				}
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

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static enum FieldDataType {
		FIELD_DATATYPE_STRING(1, "string"),
		FIELD_DATATYPE_INT(2, "int"),
		FIELD_DATATYPE_DOUBLE(3, "double"),
		FIELD_DATATYPE_OBJECT(4, "object"),
		FIELD_DATATYPE_ARRAY(5, "array");

		private int code;
		private String name;

		FieldDataType(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static int getCodeByName(String name) {
			for (FieldDataType fieldDataType : FieldDataType.values()) {
				if (fieldDataType.name.equals(name)) {
					return fieldDataType.code;
				}
			}
			return 1;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

}
