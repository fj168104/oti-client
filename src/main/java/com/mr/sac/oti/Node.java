package com.mr.sac.oti;

import com.mr.sac.oti.pack.Parser;

import java.util.Map;

/**
 * Created by feng on 18-5-6
 */
public interface Node{

	void pack(Parser parser);

	void unpack(String receivedData, Parser parser);

	void fillValue(Map<String, Object> parameters) throws Exception;

}
