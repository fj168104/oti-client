package com.mr.sac.oti.comm;

import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.biz.Handler;
import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.listen.TransactionEvent;
import com.mr.sac.oti.pack.Parser;

/**
 * Created by feng on 18-5-6
 */
public class ServerTransaction extends TransactionSupport {

	private static Log log = LogFactory.get();

	private Parser parser;

	public ServerTransaction(Message requestMessage, Message responseMessage) {
		super(requestMessage, responseMessage);
	}

	public Object communicate(Object requestObj, Handler handler){

		try {
			setExecuteStatus(EXECUTING);
			deSerializeRequestMessages(requestObj);
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_SERIAL);
			}
			//业务处理
			parameters = handler.process(requestMessage);
			//填充赋值
			responseMessage.fillValue(parameters);
			Object response = serializeResponseMessages();
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_DESERIAL);
			}

			setExecuteStatus(SUCCESS);
			return response;
		} catch (Exception e) {
			setExecuteStatus(FAIL);
			for (Listener listener : listeners) {
				listener.handle(this, TransactionEvent.EVENT_ERROR);
			}
			exceptionString = e.getMessage();
			return exceptionString;
		}
	}

	protected Object serializeResponseMessages() throws Exception {
		return responseMessage.pack(parser);
	}

	protected void deSerializeRequestMessages(Object requestObj) {
		requestMessage.unpack(requestObj, parser);
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	private void setExecuteStatus(int executeStatus) {
		this.executeStatus = executeStatus;
	}
}
