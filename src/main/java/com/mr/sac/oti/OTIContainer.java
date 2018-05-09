package com.mr.sac.oti;

import com.mr.framework.core.lang.Singleton;
import com.mr.framework.setting.Setting;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 18-5-6
 */
public abstract class OTIContainer {

	public final static String DEFAULT_CONTAINER_ClASSNAME = "com.mr.sac.oti.DefaultContainer";

	@Getter
	protected DataSource dataSource;
	/**
	 * 实例化Container
	 *
	 * @return
	 */
	public static OTIContainer getInstance() {
		return getInstance(DEFAULT_CONTAINER_ClASSNAME);
	}

	private static OTIContainer getInstance(String className) {
		return Singleton.get(className);
	}

	/**
	 * 通过远程服务来载入配置
	 *
	 * @param msgIds
	 */
	public abstract void loadRemoteConfiguration(String... msgIds);

	/**
	 * 通过本地xml载入配置
	 *
	 * @param file
	 */
	public abstract void loadLocalConfiguration(String file);


	/**
	 * 初始化数据库连接
	 * setting包含：
	 * 		showSql
	 * 		isFormatSql
	 * 		isShowParams
	 * 		username
	 * 		password
	 * 		url
	 *
	 * @param setting
	 */
	public abstract void initDataSource(Setting setting);

	/**
	 *
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
	public abstract Field newField(String fieldTag);

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
	 * @param messages
	 * @return Transaction
	 */
	public abstract Transaction newTransaction(List<Message> messages);

	/**
	 * 加入外部参数
	 * 1、解析字符串变量，如 #{param} type(Object) = String
	 * 2、解析array类型的Message数组  type(Object) = List<Message>
	 *
	 * @param params
	 */
	public abstract void addParam(Map<String, Object> params);

}
