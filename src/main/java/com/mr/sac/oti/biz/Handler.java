package com.mr.sac.oti.biz;

import com.mr.sac.oti.bean.Message;

import java.util.Map;

/**
 * Created by feng on 18-5-12
 */
public interface Handler {
	Map<String, Object> process(Message message);
}
