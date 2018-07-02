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

	public boolean communicate(String endPoint) {
		if (CollectionUtil.isEmpty(parameters)) {
			log.warn("Replaced parameter is empty.");
		}
		try {
			setExecuteStatus(EXECUTING);
			//填充赋值
			requestMessage.fillValue(parameters);
			requestObj = serializeRequestMessages();
			log.debug("request message>>>\n {}", requestObj);
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_SERIAL);
			}
			responseObj = protocolAgent.exchange(endPoint, requestObj);
			log.debug("response message>>>\n {}", responseObj);
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_DESERIAL);
			}
			deSerializeResponseMessages(responseObj);
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

	protected void deSerializeResponseMessages(Object responseObj) {
		responseMessage.unpack(responseObj, parser);
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
