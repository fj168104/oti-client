package com.mr.sac.oti.test;

import com.mr.framework.json.JSONUtil;
import com.mr.framework.log.StaticLog;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.bean.Message;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 18-5-14
 */
public class ClientTest {

	/**
	 * 默认客户端测试
	 */
	@Test
	public void testDefault() throws Exception {
		String[] messages = {"demo_msg", "msg1", "msg2"};
		String endPoint = "http://localhost:9080/";
		//获取Transaction实例
		OTIContainer otiContainer = OTIContainer.getInstance();
		//载入配置
		otiContainer.loadRemoteConfiguration(messages);
		Transaction transaction = otiContainer.newTransaction("demo_msg", "msg2");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "demo");
		params.put("col6", "第6列");
		params.put("colparam9", 5);
		params.put("colparam10", 6.8);

		transaction.addParams(params);
		if (transaction.communicate(endPoint)) {
			StaticLog.info(transaction.getResponseMessage().toString());
		} else {
			StaticLog.error(transaction.getExceptionInfo());
		}

	}
}
