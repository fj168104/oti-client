package com.mr.sac.oti;

import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.bean.Node;
import com.mr.sac.oti.biz.Handler;
import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;

import java.util.Map;

/**
 * Created by feng on 18-5-7
 */
public interface Transaction {

	public static final int PREPARE = -1;
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public static final int EXECUTING = 2;

	boolean communicate(String endPoint, ProtocolAgent agent);

	Object communicate(Object requestObj, Handler handler);

	Message getResponseMessage();

	/**
	 * 0:成功
	 * 1：失败
	 * 2：执行中
	 *
	 * @return
	 */
	int getExecuteStatus();

	String getExceptionInfo();

	/**
	 * 加入外部参数
	 * 1、解析字符串变量，如 #{param} type(Object) = String
	 * 2、解析array类型的Message数组  type(Object) = List<Message>
	 *
	 * @param parameters
	 */
	void addParams(Map<String, Object> parameters) throws Exception;

	void addListener(Listener listener);

	void setParser(Parser parser);

	void setProtocolAgent(ProtocolAgent protocolagent);
}
