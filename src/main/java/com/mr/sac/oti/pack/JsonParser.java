package com.mr.sac.oti.pack;

import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by feng on 18-5-7
 */
public class JsonParser implements Parser {

	//打包前准备的消息Map
	private Map<String, Object> preparedMessage = new LinkedHashMap<String, Object>();

	public void packField(Field field) {

	}

	public void packMessage(Message message) {

	}

	public void unpackMessage(String receivedMsg) {

	}

	public String  outputPackedString() {

		return null;
	}
}
