package com.mr.sac.oti.test;

import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.comm.server.EmbedServer;
import com.mr.sac.oti.comm.server.SocketServer;
import org.junit.Test;

/**
 * Created by feng on 18-5-15
 */
public class ServerTest {
	/**
	 * 服务端测试1,默认的设置
	 * Parser : JsonParser
	 * Protocal: HttpProtocal
	 */
	@Test
	public void testDefault() {
		EmbedServer server = new EmbedServer();
		try {
			OTIContainer container = OTIContainer.getInstance();
			container.loadRemoteConfiguration(new String[]{"demo_msg", "msg2"});
			server.startNotDaemon(9080, "demo_msg", "msg2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务端测试2，测试XmlParser
	 * Parser : XmlParser
	 * Protocal: HttpProtocal
	 */
	@Test
	public void testXml() {
		EmbedServer server = new EmbedServer();
		EmbedServer.parser = OTIContainer.XML_PARSER;
		try {
			OTIContainer container = OTIContainer.getInstance();
			container.loadRemoteConfiguration(new String[]{"demo_msg", "msg2"});
			server.startNotDaemon(9080, "demo_msg", "msg2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务端测试3，测试TcpAgent
	 * Protocal: Socket 连接
	 */
	@Test
	public void testTcpAgent() {
		try {
			OTIContainer container = OTIContainer.getInstance();
			container.loadRemoteConfiguration(new String[]{"demo_msg", "msg2"});
			SocketServer.startNotDaemon(7878, "demo_msg", "msg2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
