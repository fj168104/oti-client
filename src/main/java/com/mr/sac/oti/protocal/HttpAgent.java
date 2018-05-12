package com.mr.sac.oti.protocal;

import com.mr.framework.http.HttpUtil;

/**
 * Created by feng on 18-5-10
 */
public class HttpAgent implements ProtocolAgent {
	@Override
	public String exchange(String mString) {
		return HttpUtil.get(mString);
	}
}
