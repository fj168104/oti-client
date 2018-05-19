package com.mr.sac.oti.pack;

import com.mr.framework.json.JSONArray;
import com.mr.framework.json.JSONObject;
import com.mr.framework.json.JSONUtil;
import com.mr.sac.oti.DataType;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-5-7
 */
public class JsonParser implements Parser {

	public Object packMessage(Message message) {
		return transFormMessageToJson(message).toString();
	}

	private JSONObject transFormMessageToJson(Message message) {
		//打包前准备的消息Map
		JSONObject jsonObject = JSONUtil.createObj();

		for (Map.Entry<String, Field> entry : message.getFieldMap().entrySet()) {
			String fieldTag = entry.getKey();
			Field field = entry.getValue();
			String value = null;

			switch (DataType.getCodeByName(field.getDataType())) {
				case 1:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					jsonObject.put(fieldTag, value);
					break;
				case 2:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					jsonObject.put(fieldTag, Integer.parseInt(value));
					break;
				case 3:
					if (Objects.isNull(field.getValue())) continue;
					value = String.valueOf(field.getValue());
					//TODO length 处理
					jsonObject.put(fieldTag, Double.parseDouble(value));
					break;
				case 4:
					jsonObject.put(fieldTag, transFormMessageToJson(field.getObjectMessage()));
					break;
				case 5:
					JSONArray jsonArray = JSONUtil.createArray();
					jsonObject.put(fieldTag, jsonArray);
					for (Message mess : field.getArrayMessage()) {
						jsonArray.add(transFormMessageToJson(mess));
					}
					break;
				default:
					break;
			}
		}
		return jsonObject;
	}

	public void unpackMessage(Object receivedMsg, Message message) {
		JSONObject jsonObject = JSONUtil.parseObj((String) receivedMsg);
		transFormJsonToMessage(jsonObject, message);

	}

	private void transFormJsonToMessage(JSONObject jsonObject, Message message) {
		LinkedHashMap<String, Field> fieldMap = message.getFieldMap();
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			String fieldTag = entry.getKey();
			Field field = entry.getValue();
			String value = null;

			switch (DataType.getCodeByName(field.getDataType())) {
				case 1:
					if (Objects.isNull(jsonObject.get(fieldTag))) continue;
					value = String.valueOf(jsonObject.get(fieldTag));
					field.setValue(value);
					break;
				case 2:
					if (Objects.isNull(jsonObject.get(fieldTag))) continue;
					value = String.valueOf(jsonObject.get(fieldTag));
					//TODO length 处理
					field.setValue(Integer.parseInt(value));
					break;
				case 3:
					if (Objects.isNull(jsonObject.get(fieldTag))) continue;
					value = String.valueOf(jsonObject.get(fieldTag));
					//TODO length 处理
					field.setValue(Double.parseDouble(value));
					break;
				case 4:
					field.setObjectMessage(field.getMessageTemplete().clone());
					transFormJsonToMessage(jsonObject.getJSONObject(fieldTag), field.getObjectMessage());
					break;
				case 5:
					JSONArray jsonArray = jsonObject.getJSONArray(fieldTag);
					for (int i = 0; i < jsonArray.size(); i++) {
						Message msg = field.getMessageTemplete().clone();
						field.getArrayMessage().add(msg);
						JSONObject jObj = (JSONObject) jsonArray.get(i);
						transFormJsonToMessage(jObj, msg);
					}
					break;
				default:
					break;
			}
		}
	}

}
