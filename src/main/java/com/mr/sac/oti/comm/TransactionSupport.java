package com.mr.sac.oti.comm;

import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.biz.Handler;
import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.protocal.ProtocolAgent;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by feng on 18-5-6
 */
public abstract class TransactionSupport implements Transaction {

	protected volatile int executeStatus = PREPARE;

	protected String exceptionString;

	protected static List<Listener> listeners = new ArrayList<Listener>();

	protected Message requestMessage;

	protected Message responseMessage;

	protected DataSource dataSource = OTIContainer.getDataSource();

	protected Map<String, Object> parameters;

	public TransactionSupport(Message requestMessage, Message responseMessage) {
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
	}

	/**
	 * 客户端交互
	 * @param endPoint
	 * @return
	 */
	public boolean communicate(String endPoint){
		return false;
	}

	/**
	 * 服务端交互
	 * @param requestObj
	 * @return
	 */
	public Object communicate(Object requestObj, Handler handler){
		return null;
	}

	@Override
	public Message getResponseMessage() {
		return responseMessage;
	}

	@Override
	public String getExceptionInfo() {
		return exceptionString;
	}


	public int getExecuteStatus() {
		return executeStatus;
	}

	@Override
	public void setProtocolAgent(ProtocolAgent protocolagent) {

	}
	/**
	 * 增加监听器,
	 * 例如记录相关调用日志等等
	 *
	 * @param listener
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void addParams(Map<String, Object> parameters) throws Exception{
		this.parameters = Collections.unmodifiableMap(parameters);

	}
}
