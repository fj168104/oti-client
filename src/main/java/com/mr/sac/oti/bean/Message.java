package com.mr.sac.oti.bean;

import com.mr.sac.oti.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mr.framework.core.clone.Cloneable;
import com.mr.sac.oti.pack.Parser;
import lombok.Data;

/**
 * Created by feng on 18-5-6
 */
@Data
public class Message implements Node, Cloneable {

	private String id;

	private String description;

	private LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();

	public void pack(Parser parser) {
		parser.packMessage(this);
	}

	public void unpack(String receivedData, Parser parser) {
		parser.unpackMessage(receivedData);
	}

	@Override
	public void fillValue(Map<String, Object> parameters) throws Exception {
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			entry.getValue().fillValue(parameters);
		}
	}

	public Message clone() {
		Message message = new Message();
		message.id = id;
		message.description = description;
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			message.fieldMap.put(entry.getKey(), entry.getValue().clone());
		}
		return message;
	}

	public Field getField(String fieldTag){
		return fieldMap.get(fieldTag);
	}

}
