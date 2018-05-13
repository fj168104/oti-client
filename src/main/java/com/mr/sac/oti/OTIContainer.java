package com.mr.sac.oti;

import com.mr.framework.core.lang.Singleton;
import com.mr.framework.setting.Setting;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;

/**
 * Created by feng on 18-5-6
 */
public abstract class OTIContainer {

	private static String DEFAULT_CONTAINER_ClASSNAME = "com.mr.sac.oti.DefaultContainer";
	protected static String DEFAULT_CONFIG_CENTER = "http://106.14.195.171:8086";

	protected static String CONFIG_MESSAGE_URL_PREFIX = "/api/v1/oti_msg/";
	private static String CONTAINER_ClASSNAME = DEFAULT_CONTAINER_ClASSNAME;

	public final static int ROLE_SERVER = 0x0;
	public final static int ROLE_CLIENT = 0x1;

	public static void setContainerClass(String containerClass) {
		CONTAINER_ClASSNAME = containerClass;
	}

	//SAC配置中心地址 http://[ip]:port
	@Setter
	protected String configCenterUrl = DEFAULT_CONFIG_CENTER;

	@Getter
	protected static DataSource dataSource;

	/**
	 * 实例化Container
	 *
	 * @return
	 */
	public static OTIContainer getInstance() {
		return getInstance(CONTAINER_ClASSNAME);
	}

	private static OTIContainer getInstance(String className) {
		return Singleton.get(className);
	}

	/**
	 * 通过远程服务来载入配置
	 *
	 * @param msgIds
	 */
	public abstract void loadRemoteConfiguration(String[] msgIds);

	/**
	 * 通过本地xml载入配置=
	 *
	 * @param file
	 */
	public abstract void loadLocalConfiguration(String file);


	/**
	 * 初始化数据库连接
	 * setting包含：
	 * showSql
	 * isFormatSql
	 * isShowParams
	 * username
	 * password
	 * url
	 *
	 * @param setting
	 */
	public abstract void initDataSource(Setting setting);

	/**
	 * @param dsConfigId 远程配置载入
	 */
	public abstract void initDataSource(String dsConfigId);

	/**
	 * 本地配置文件载入
	 * 例如：
	 * ## 基本配置信息
	 * # JDBC URL，根据不同的数据库，使用相应的JDBC连接字符串
	 * url = jdbc:mysql://<host>:<port>/<database_name>
	 * # 用户名，此处也可以使用 user 代替
	 * username = 用户名
	 * # 密码，此处也可以使用 pass 代替
	 * password = 密码
	 * # JDBC驱动名，可选（Hutool会自动识别）
	 * driver = com.mysql.jdbc.Driver
	 */
	public abstract void initDataSource();

	public abstract void initDataSource(DataSource dataSource);

	/**
	 * 创建 Field
	 *
	 * @param fieldTag
	 * @return Field
	 */
	public abstract Field newField(String messageId, String fieldTag);

	/**
	 * 创建 Message
	 *
	 * @param messageId
	 * @return Message
	 */
	public abstract Message newMessage(String messageId);

	/**
	 * 创建 Transaction
	 *
	 * @param requestMessageId
	 * @param responseMessageId
	 * @return Transaction
	 */
	public abstract Transaction newTransaction(String requestMessageId,
											   String responseMessageId,
											   ProtocolAgent agent,
											   Parser parser);

	public abstract Transaction newTransaction(String requestMessageId,
											   String responseMessageId,
											   ProtocolAgent agent);

	public abstract Transaction newTransaction(String requestMessageId,
											   String responseMessageId);

	/**
	 * 服务端Transaction
	 *
	 * @param requestMessageId
	 * @param responseMessageId
	 * @return
	 */
	public abstract Transaction newServiceTransaction(String requestMessageId,
													  String responseMessageId);

	/**
	 * 服务端Transaction
	 *
	 * @param requestMessageId
	 * @param responseMessageId
	 * @param parser
	 * @return
	 */

	public abstract Transaction newServiceTransaction(String requestMessageId,
													  String responseMessageId,
													  Parser parser);


}
