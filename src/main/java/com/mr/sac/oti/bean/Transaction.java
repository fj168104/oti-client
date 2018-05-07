package com.mr.sac.oti.bean;

import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;

/**
 * Created by feng on 18-5-7
 */
public interface Transaction {

	public static final int PREPARE = -1;
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public static final int EXECUTING = 2;

	boolean communicate(ProtocolAgent agent);

	void setRequestMessage(Message message);

	void setResponseMessage(Message message);

	/**
	 * 0:成功
	 * 1：失败
	 * 2：执行中
	 * @return
	 */
	int getExecuteStatus();

	String getExceptionMessage();

	void addListener(Listener listener);

	void setParser(Parser parser);
}
