package com.mr.sac.oti.bean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.mr.framework.core.clone.Cloneable;
import com.mr.sac.oti.pack.Parser;
import lombok.Data;

/**
 * Created by feng on 18-5-6
 */
@Data
public class Message implements Node, Packable, Cloneable {

	public static LinkedHashMap<String, Message> messageMap = new LinkedHashMap<>();

	private String id;

	private String description;

	private String encoding;

	private LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();

	public Object pack(Parser parser) {
		return parser.packMessage(this);
	}

	public void unpack(Object receivedData, Parser parser) {
		parser.unpackMessage(receivedData, this);
	}

	@Override
	public void fillValue(Map<String, Object> parameters) throws Exception {
		if (Objects.isNull(parameters)) parameters = new LinkedHashMap<>();
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			entry.getValue().fillValue(parameters);
		}
	}

	public Message clone() {
		Message message = new Message();
		message.id = id;
		message.description = description;
		message.encoding = encoding;
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			message.fieldMap.put(entry.getKey(), entry.getValue().clone());
		}
		return message;
	}

	public Field getField(String fieldTag) {
		return fieldMap.get(fieldTag);
	}

	@Override
	public String toString() {
		return fieldMap.toString();
	}

}