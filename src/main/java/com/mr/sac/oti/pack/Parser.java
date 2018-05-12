package com.mr.sac.oti.pack;

import com.mr.sac.oti.bean.Message;

/**
 * Created by feng on 18-5-7
 */
public interface Parser {

	Object packMessage(Message message);

	void unpackMessage(Object receivedMsg, Message message);
}
