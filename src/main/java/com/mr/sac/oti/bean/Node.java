package com.mr.sac.oti.bean;

import java.util.Map;

/**
 * Created by feng on 18-5-6
 */
public interface Node{

	/**
	 * 加入外部参数
	 * 1、解析字符串变量，如 #{param} type(Object) = String
	 * 2、解析array类型的Message数组  type(Object) = List<Message>
	 *
	 * @param parameters
	 */
	void fillValue(Map<String, Object> parameters) throws Exception;

}
