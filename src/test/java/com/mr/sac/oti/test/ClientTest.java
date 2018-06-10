package com.mr.sac.oti.test;

import com.mr.framework.db.ds.DSFactory;
import com.mr.framework.json.JSONUtil;
import com.mr.framework.log.StaticLog;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.pack.JsonParser;
import com.mr.sac.oti.protocal.HttpAgent;
import org.junit.Test;

import javax.sql.DataSource;
import java.awt.font.TransformAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 18-5-14
 */
public class ClientTest {

	private String[] messages = {"demo_msg", "msg1", "msg2"};
	private String endPoint = "http://localhost:9080/";
	private String tcpEndPoint = "localhost:7878";

	/**
	 * 默认客户端测试,通过远程载入配置
	 */
	@Test
	public void testDefault() throws Exception {
		OTIContainer otiContainer = createOTIContainer();

		Transaction transaction = otiContainer.newTransaction("demo_msg", "msg2");

		action(transaction, endPoint);
	}

	/**
	 * 默认客户端测试,通过本地配置文件初始化
	 */
	@Test
	public void testLocalConfigLoad() throws Exception {
		//获取Transaction实例
		OTIContainer otiContainer = OTIContainer.getInstance();
		//载入配置
		otiContainer.loadLocalConfiguration(OTIContainer.configXML);

		Transaction transaction = otiContainer.newTransaction("demo_msg", "msg2");

		action(transaction, endPoint);
	}



	/**
	 * 客户端测试,XML解析器
	 * parser:XMLParsers
	 */
	@Test
	public void testXMLParser() throws Exception {

		OTIContainer otiContainer = createOTIContainer();

		Transaction transaction = otiContainer.newTransaction("demo_msg",
				"msg2",
				OTIContainer.XML_PARSER);

		action(transaction, endPoint);
	}

	/**
	 * 客户端测试,自定义解析器
	 * parser:JsonParser
	 */
	@Test
	public void testCustomizedParser() throws Exception {
		OTIContainer otiContainer = createOTIContainer();

		//外部导入Parser（JsonParser）,可以自定义解析器
		Transaction transaction = otiContainer.newTransaction("demo_msg",
				"msg2",
				new JsonParser());

		action(transaction, endPoint);
	}

	/**
	 * 客户端测试,自定义通信协议处理
	 * ProtocolAgent:HttpAgent
	 */
	@Test
	public void testCustomizedProtocolAgent() throws Exception {
		OTIContainer otiContainer = createOTIContainer();

		//外部导入Parser（JsonParser）,可以自定义解析器
		Transaction transaction = otiContainer.newTransaction("demo_msg",
				"msg2",
				new HttpAgent());

		action(transaction, endPoint);
	}


	/**
	 * 客户端测试,socket 方式
	 * Agent:ProtocolAgent
	 */
	@Test
	public void testTcpAgent() throws Exception {
		OTIContainer otiContainer = createOTIContainer();

		Transaction transaction = otiContainer.newTransaction("demo_msg",
				"msg2",
				OTIContainer.TCP_AGENT);

		action(transaction, tcpEndPoint);
	}

	/**
	 * 设置数据源
	 * @throws Exception
	 */
	@Test
	public void testOutterDatasource() throws Exception {
		DataSource dataSource = DSFactory.get();
		//获取Transaction实例
		OTIContainer otiContainer = OTIContainer.getInstance();

		//外部注入数据源
		otiContainer.initDataSource(dataSource);
		//载入配置
		otiContainer.loadRemoteConfiguration(messages);

		Transaction transaction = otiContainer.newTransaction("demo_msg", "msg2");

		action(transaction, endPoint);
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

	/**
	 * 组包、发送、解包
	 *
	 * @param transaction
	 * @throws Exception
	 */
	private void action(Transaction transaction, String endPoint) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "demo");
		params.put("col6", "第6列");
		params.put("colparam9", 5);
		params.put("colparam10", 6.8);
		params.put("col12", "字段12");

		transaction.addParams(params);
		if (transaction.communicate(endPoint)) {
			StaticLog.info(transaction.getResponseMessage().toString());
		} else {
			StaticLog.error(transaction.getExceptionInfo());
		}
	}

}
