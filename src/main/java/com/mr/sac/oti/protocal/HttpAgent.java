package com.mr.sac.oti.protocal;

import com.mr.framework.http.HttpUtil;

import java.util.HashMap;

/**
 * Created by feng on 18-5-10
 */
public class HttpAgent implements ProtocolAgent {
	@Override
	public Object exchange(String endPoint, Object mObject) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("request", mObject);

		return HttpUtil.get(endPoint, paramMap);
	}
}
