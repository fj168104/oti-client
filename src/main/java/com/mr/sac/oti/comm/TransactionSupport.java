package com.mr.sac.oti.comm;

import com.mr.sac.oti.Dbable;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.listen.Listener;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 18-5-6
 */
public abstract class TransactionSupport implements Transaction, Dbable{

	protected volatile int executeStatus = PREPARE;

	protected String exceptionString;

	protected static List<Listener> listeners = new ArrayList<Listener>();

	protected Message requestMessage;

	protected Message responseMessage;

	protected DataSource dataSource;

	public void setRequestMessage(Message message) {
		requestMessage = message;
	}

	public void setResponseMessage(Message message) {
		responseMessage = message;
	}

	public String getExceptionMessage(){
		return exceptionString;
	}


	public int getExecuteStatus() {
		return executeStatus;
	}

	/**
	 * 增加监听器,
	 * 例如记录相关调用日志等等
	 * @param listener
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

}
