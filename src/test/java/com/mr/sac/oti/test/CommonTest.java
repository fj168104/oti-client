package com.mr.sac.oti.test;

import com.mr.framework.log.StaticLog;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.bean.Message;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 18-6-6
 */
public class CommonTest {

	private String[] messages = {"demo_msg", "msg1", "msg2"};

	/**
	 * 创建并填充Message
	 */
	@Test
	public void testDefault() throws Exception {

		OTIContainer otiContainer = createOTIContainer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "demo");
		params.put("col6", "第6列");
		params.put("colparam9", 5);
		params.put("colparam10", 6.8);
		Message message = otiContainer.newMessage("demo_msg");
		// 填充之前
		StaticLog.info("填充之前:");
		StaticLog.info(message.toString());
		// 填充之后=
		message.fillValue(params);
		StaticLog.info("填充之后:");
		StaticLog.info(message.toString());

	}

	/**
	 * 创建 OTIContainer
	 *
	 * @return OTIContainer
	 */
	private OTIContainer createOTIContainer() {

		//获取Transaction实例
		OTIContainer otiContainer = OTIContainer.getInstance();
		//载入配置
		otiContainer.loadRemoteConfiguration(messages);
		return otiContainer;
	}
}
