package com.mr.sac.oti.comm;

import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.listen.TransactionEvent;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;

/**
 * Created by feng on 18-5-6
 */
public class ClientTransaction extends TransactionSupport {

	private Parser parser;

	private ProtocolAgent protocolAgent;

	public ClientTransaction(Message requestMessage, Message responseMessage) {
		super(requestMessage, responseMessage);
	}

	public boolean communicate(ProtocolAgent agent) {
		try {
			setExecuteStatus(EXECUTING);
			String request = serializeRequestMessages();
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_SERIAL);
			}
			String result = agent.process(request);
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

	protected String serializeRequestMessages() {
		requestMessage.pack(parser);
		return parser.outputPackedString();
	}

	protected void deSerializeResponseMessages(String responseString) {
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
