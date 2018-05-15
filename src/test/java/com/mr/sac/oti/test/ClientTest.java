package com.mr.sac.oti.test;

import com.mr.framework.json.JSONUtil;
import com.mr.framework.log.StaticLog;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.Transaction;
import org.junit.Test;

/**
 * Created by feng on 18-5-14
 */
public class ClientTest {

	/**
	 * 默认客户端测试
	 */
	@Test
	public void testDefault() {
		String[] messages = {"demo_msg", "msg1", "msg2"};
		String endPoint = "http://localhost:9080/";
		//获取Transaction实例
		OTIContainer otiContainer = OTIContainer.getInstance();
		//载入配置
		otiContainer.loadRemoteConfiguration(messages);
		Transaction transaction = otiContainer.newTransaction("demo_msg", "msg1");

		if(transaction.communicate(endPoint)){
			StaticLog.info(JSONUtil.toJsonStr(transaction.getResponseMessage()));
		}else {
			StaticLog.error(transaction.getExceptionInfo());
		}


	}
}
