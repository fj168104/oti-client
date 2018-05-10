package com.mr.sac.oti;

import com.mr.framework.core.lang.Singleton;
import com.mr.framework.setting.Setting;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by feng on 18-5-6
 */
public abstract class OTIContainer {

	private static String DEFAULT_CONTAINER_ClASSNAME = "com.mr.sac.oti.DefaultContainer";

	private static String CONTAINER_ClASSNAME = DEFAULT_CONTAINER_ClASSNAME;

	public final static int ROLE_SERVER = 0x0;
	public final static int ROLE_CLIENT = 0x1;

	public static void setContainerClass(String containerClass) {
		CONTAINER_ClASSNAME = containerClass;
	}

	@Getter
	protected DataSource dataSource;

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
	 * 通过本地xml载入配置
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
	 */
	public abstract void initDataSource();

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

}
