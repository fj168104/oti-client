package com.mr.sac.oti;

import com.mr.framework.core.io.resource.NoResourceException;
import com.mr.framework.core.util.XmlUtil;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
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
							String messageUrl = configCenterUrl + CONFIG_MESSAGE_URL_PREFIX + messageId;
							JSONObject joAll = JSONUtil.parseObj(HttpUtil.get(messageUrl));
							JSONObject jsonObject = joAll.getJSONObject("message");
							message.setDescription(jsonObject.get("Description", String.class));
							if (!Objects.isNull(jsonObject.get("Encoding"))) {
								message.setEncoding(jsonObject.get("Encoding", String.class));
							}
							message.setFieldMap(parseJsonToFields(jsonObject.getJSONObject("Fields")));
							messageMap.put(messageId, message);
						}
					} else {
						String config = (String) object;
						Document docResult = XmlUtil.readXML(config);
						Element elementDoc = XmlUtil.getRootElement(docResult);
						Element messageList = XmlUtil.getElement(elementDoc, "MessageList");
						List<Element> messageElements = XmlUtil.getElements(messageList, "Message");
						for (Element element : messageElements) {
							Message message = new Message();
							message.setId(element.getAttribute("Id"));
							message.setDescription(element.getAttribute("Description"));
							message.setEncoding(element.getAttribute("CharSet"));
							message.setFieldMap(parseXmlToFields(XmlUtil.getElements(element, "Field")));
							messageMap.put(message.getId(), message);
						}
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
	private LinkedHashMap<String, Field> parseJsonToFields(JSONObject fieldObj) {
		LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : fieldObj.entrySet()) {
			Field toField = new Field();
			String fieldTag = entry.getKey();
			JSONObject field = (JSONObject) entry.getValue();
			//init Field
			toField.setFieldTag(fieldTag);
			toField.setDescription(field.getStr("Description", ""));
			toField.setDataType(field.getStr("DataType", "string"));

			if (field.getStr("DataType", "string").equals(DataType.ARRAY.name)
					|| field.getStr("DataType", "string").equals(DataType.OBJECT.name)) {
				if (!Objects.isNull(field.get("TableField"))) {
					toField.setTableField(field.getStr("TableField", ""));
				}
				Message message = new Message();
				message.setId(toField.getFieldTag());
				message.setDescription(toField.getDescription());
				message.setFieldMap(parseJsonToFields(field.getJSONObject("Fields")));
				toField.setMessageTemplete(message);
			} else {
				if (toField.getDataType().equals(DataType.DOUBLE.name)) {
					String[] lens = field.getStr("Length", "0").split(",");
					toField.setLength(Integer.parseInt(lens[0]));
					toField.setDecimalLength(Integer.parseInt(lens[1]));
				} else if (!toField.getDataType().equals(DataType.BOOL.name)) {
					toField.setLength(field.get("Length", Integer.class));
				}

				toField.setDefaultValue(field.get("DefaultValue", String.class));
				toField.setRequire(field.get("IsRequire", Boolean.class));
			}
			fieldMap.put(toField.getFieldTag(), toField);
		}
		return fieldMap;
	}

	/**
	 * 解析key=Fields
	 *
	 * @param fieldElements
	 * @return
	 */
	private LinkedHashMap<String, Field> parseXmlToFields(List<Element> fieldElements) {
		LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
		for (Element fieldElement : fieldElements) {
			Field toField = new Field();
			toField.setFieldTag(fieldElement.getAttribute("FieldTag"));
			toField.setDescription(fieldElement.getAttribute("Description"));
			toField.setDataType(fieldElement.getAttribute("DataType"));

			if (toField.getDataType().equals(DataType.ARRAY.name)
					|| toField.getDataType().equals(DataType.OBJECT.name)) {
				if (!Objects.isNull(fieldElement.getAttribute("TableField"))) {
					toField.setTableField(fieldElement.getAttribute("TableField"));
				}
				Message message = new Message();
				message.setId(toField.getFieldTag());
				message.setDescription(toField.getDescription());
				Element sElement = XmlUtil.getElement(fieldElement, "Message");
				List<Element> fElements = XmlUtil.getElements(sElement, "Field");
				message.setFieldMap(parseXmlToFields(fElements));
				toField.setMessageTemplete(message);
			} else {
				if (toField.getDataType().equals(DataType.DOUBLE.name)) {
					String[] lens = fieldElement.getAttribute("Length").split(",");
					toField.setLength(Integer.parseInt(lens[0]));
					toField.setDecimalLength(Integer.parseInt(lens[1]));
				} else {
					toField.setLength(Integer.parseInt(fieldElement.getAttribute("Length")));
				}

				toField.setDefaultValue(fieldElement.getAttribute("DefaultValue"));
				toField.setRequire("Y".equals(fieldElement.getAttribute("IsRequire")));
			}
			fieldMap.put(toField.getFieldTag(), toField);
		}

		return fieldMap;
	}
}
