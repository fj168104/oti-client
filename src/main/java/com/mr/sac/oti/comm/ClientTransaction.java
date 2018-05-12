package com.mr.sac.oti.comm;

import com.mr.framework.core.collection.CollectionUtil;
import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.listen.TransactionEvent;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;

/**
 * Created by feng on 18-5-6
 */
public class ClientTransaction extends TransactionSupport {

	private static Log log = LogFactory.get();

	private Parser parser;

	private ProtocolAgent protocolAgent;

	public ClientTransaction(Message requestMessage, Message responseMessage) {
		super(requestMessage, responseMessage);
	}

	public boolean communicate(ProtocolAgent agent) {
		if(CollectionUtil.isEmpty(parameters)){
			log.warn("Repalced parameter is empty.");
		}
		try {
			setExecuteStatus(EXECUTING);
			//填充赋值
			requestMessage.fillValue(parameters);
			String request = (String) serializeRequestMessages();
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_SERIAL);
			}
			String result = agent.exchange(request);
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_DESERIAL);
			}
			deSerializeResponseMessages(result);
			setExecuteStatus(SUCCESS);
			return true;
		} catch (Exception e) {
			setExecuteStatus(FAIL);
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_ERROR);
			}
			exceptionString = e.getMessage();
			return false;
		}
	}

	protected Object serializeRequestMessages() throws Exception {
		return requestMessage.pack(parser);
	}

	protected void deSerializeResponseMessages(Object responseString) {
		responseMessage.unpack(responseString, parser);
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	@Override
	public void setProtocolAgent(ProtocolAgent protocolagent) {
		this.protocolAgent = protocolagent;
	}

	private void setExecuteStatus(int executeStatus) {
		this.executeStatus = executeStatus;
	}
}
