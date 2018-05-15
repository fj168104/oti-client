package com.mr.sac.oti.test;

import com.mr.sac.oti.comm.server.EmbedServer;
import org.junit.Test;

/**
 * Created by feng on 18-5-15
 */
public class ServerTest {
	/**
	 * 服务端测试
	 */
	@Test
	public void testDefault() {
		EmbedServer server = new EmbedServer();
		try {
			server.startNotDaemon(9080, "demo_msg", "msg1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
