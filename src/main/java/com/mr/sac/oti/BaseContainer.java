package com.mr.sac.oti;

import com.mr.framework.core.io.resource.NoResourceException;
import com.mr.framework.db.ds.DSFactory;
import com.mr.framework.http.HttpUtil;
import com.mr.framework.json.JSONObject;
import com.mr.framework.json.JSONUtil;
import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.framework.setting.Setting;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-5-9
 */
public abstract class BaseContainer extends OTIContainer {

	protected LinkedHashMap<String, Message> messageMap = Message.messageMap;
	protected boolean isLoad = false;

	private static Log log = LogFactory.get();

	@Override
	public void loadRemoteConfiguration(String[] msgIds) {
		doLoadConfig(msgIds);
	}

	@Override
	public void loadLocalConfiguration(String file) {
		doLoadConfig(file);
	}

	@Override
	public synchronized void initDataSource(Setting setting) {
		if (Objects.isNull(dataSource)) {
			dataSource = DSFactory.create(setting).getDataSource();
		}
	}

	@Override
	public synchronized void initDataSource() {
		if (Objects.isNull(dataSource)) {
			dataSource = DSFactory.get();
		}
	}

	@Override
	public Field newField(String messageId, String fieldTag) {
		checkLoaded();
		Field field = messageMap.get(messageId).getField(fieldTag).clone();
		return field;
	}

	@Override
	public Message newMessage(String messageId) {
		checkLoaded();
		return messageMap.get(messageId).clone();
	}

	public Transaction newTransaction(String requestMessageId, String responseMessageId) {
		return null;
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent) {
		return null;
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  Parser parser) {
		return null;
	}


	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent,
									  Parser parser) {

		return null;
	}

	public Transaction newServiceTransaction(String requestMessageId,
											 String responseMessageId) {
		return null;
	}

	public Transaction newServiceTransaction(String requestMessageId,
											 String responseMessageId,
											 Parser parser) {
		return null;
	}

	protected void checkTransactionAble(String requestMessageId,
										String responseMessageId) {
		checkLoaded();
		if (!messageMap.containsKey(requestMessageId) || !messageMap.containsKey(responseMessageId)) {
			throw new RuntimeException("requestMessageId or responseMessageId is not being loaded");
		}
	}

	private void checkLoaded() {
		if (!isLoad) {
			throw new RuntimeException("OTI config is not load.");
		}
	}

	public void initDataSource(String dsConfigId) {
	}

	public void initDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void doLoadConfig(Object object) {
		if (!isLoad) {
			synchronized (this) {
				if (!isLoad) {
					if (Objects.isNull(dataSource)) {
						try {
							initDataSource();
						} catch (NoResourceException e) {
							log.warn(e.getMessage());
						}
					}
					if (Objects.isNull(dataSource)) log.warn("dataSource is not init");
					if (object instanceof String[]) {
						for (String messageId : (String[]) object) {
							Message message = new Message();
							message.setId(messageId);
							String messageUrl = DEFAULT_CONFIG_CENTER + CONFIG_MESSAGE_URL_PREFIX + messageId;
							JSONObject joAll = JSONUtil.parseObj(HttpUtil.get(messageUrl));
							JSONObject jsonObject = joAll.getJSONObject("message");
							message.setDescription(jsonObject.get("Description", String.class));
							if (!Objects.isNull(jsonObject.get("Encoding"))) {
								message.setEncoding(jsonObject.get("Encoding", String.class));
							}
							message.setFieldMap(parseToFields(jsonObject.getJSONObject("Fields")));
							messageMap.put(messageId, message);
						}
					} else {
						//TODO 解析 xml 配置
					}
					isLoad = true;
				}

			}
		}
	}

	/**
	 * 解析key=Fields
	 *
	 * @param fieldObj
	 * @return
	 */
	private LinkedHashMap<String, Field> parseToFields(JSONObject fieldObj) {
		LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : fieldObj.entrySet()) {
			Field toField = new Field();
			String fieldTag = entry.getKey();
			JSONObject field = (JSONObject) entry.getValue();
			//init Field
			toField.setFieldTag(fieldTag);
			toField.setDescription(field.get("Description", String.class));
			toField.setDataType(field.get("DataType", String.class));

			if (field.get("DataType", String.class).equals(DataType.ARRAY.name)
					|| field.get("DataType", String.class).equals(DataType.OBJECT.name)) {
				if (!Objects.isNull(field.get("TableField"))) {
					toField.setTableField(field.get("TableField", String.class));
				}
				Message message = new Message();
				message.setId(toField.getFieldTag());
				message.setDescription(toField.getDescription());
				message.setFieldMap(parseToFields(field.getJSONObject("Fields")));
				toField.setMessageTemplete(message);
			} else {
				if (toField.getDataType().equals(DataType.DOUBLE.name)) {
					String[] lens = field.get("Length", String.class).split(",");
					toField.setLength(Integer.parseInt(lens[0]));
					toField.setDecimalLength(Integer.parseInt(lens[1]));
				} else {
					toField.setLength(field.get("Length", Integer.class));
				}

				toField.setDefaultValue(field.get("DefaultValue", String.class));
				toField.setRequire(field.get("IsRequire", Boolean.class));
			}
			fieldMap.put(toField.getFieldTag(), toField);
		}
		return fieldMap;
	}
}
