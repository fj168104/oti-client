package com.mr.sac.oti.pack;

import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;

/**
 * Created by feng on 18-5-7
 */
public interface Parser {

	void packField(Field field);

	void packMessage(Message message);

	void unpackMessage(String receivedMsg);

	String outputPackedString();
}
