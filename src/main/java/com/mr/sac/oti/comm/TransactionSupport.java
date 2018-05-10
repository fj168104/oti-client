package com.mr.sac.oti.comm;

import com.mr.sac.oti.Dbable;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.listen.Listener;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by feng on 18-5-6
 */
public abstract class TransactionSupport implements Transaction, Dbable {

	protected volatile int executeStatus = PREPARE;

	protected String exceptionString;

	protected static List<Listener> listeners = new ArrayList<Listener>();

	protected Message requestMessage;

	protected Message responseMessage;

	protected DataSource dataSource;

	protected Map<String, Object> params;

	public TransactionSupport(Message requestMessage, Message responsessMessage) {
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
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

	/**
	 * 增加监听器,
	 * 例如记录相关调用日志等等
	 *
	 * @param listener
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void addParam(Map<String, Object> params) {
		this.params = Collections.unmodifiableMap(params);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
