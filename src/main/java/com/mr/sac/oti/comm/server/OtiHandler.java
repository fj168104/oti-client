package com.mr.sac.oti.comm.server;

import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.biz.Handler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * oti业务处理类
 */
public class OtiHandler implements Handler {

	@Override
	public Map<String, Object> process(Message message) {
		Map<String, Object> map = new LinkedHashMap<>();
		return map;
	}
}