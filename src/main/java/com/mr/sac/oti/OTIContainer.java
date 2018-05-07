package com.mr.sac.oti;

import com.mr.sac.oti.bean.Transaction;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 18-5-6
 */
public interface OTIContainer {

	/**
	 * 载入配置
	 * @param config
	 */
	void loadConfiguration(Object config);

	/**
	 * 初始化数据库连接
	 * @param dataSource
	 */
	void initDataSource(DataSource dataSource);

	/**
	 * 创建 Field
	 * @param fieldTag
	 * @return Field
	 */
	Field newField(String fieldTag);

	/**
	 * 创建 Message
	 * @param messageId
	 * @return Message
	 */
	Message newMessage(String messageId);

	/**
	 * 创建 Transaction
	 * @param messages
	 * @return Transaction
	 */
	Transaction newTransaction(List<Message> messages);

	/**
	 * 加入外部参数
	 * 1、解析字符串变量，如 #{param} type(Object) = String
	 * 2、解析array类型的Message数组  type(Object) = List<Message>
	 *
	 * @param params
	 */
	void addParam(Map<String, Object> params);

}
