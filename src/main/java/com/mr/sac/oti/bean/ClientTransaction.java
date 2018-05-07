package com.mr.sac.oti.bean;

import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.listen.TransactionEvent;
import com.mr.sac.oti.protocal.ProtocolAgent;

import java.util.List;

/**
 * Created by feng on 18-5-6
 */
public abstract class ClientTransaction extends TransactionSupport {

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

	protected abstract String serializeRequestMessages();

	protected abstract void deSerializeResponseMessages(String responseString);

	private void setExecuteStatus(int executeStatus){
		this.executeStatus = executeStatus;
	}
}
