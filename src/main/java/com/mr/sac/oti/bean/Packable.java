package com.mr.sac.oti.bean;

import com.mr.sac.oti.pack.Parser;

/**
 * Created by feng on 18-5-12
 */
public interface Packable {

	Object pack(Parser parser);

	void unpack(Object receivedData, Parser parser);
}
