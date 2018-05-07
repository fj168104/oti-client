package com.mr.sac.oti;

import com.mr.sac.oti.pack.Parser;

/**
 * Created by feng on 18-5-6
 */
public interface Node{

	void addNode(Node node);

	void pack(Parser parser);

	void unpack(String receivedData, Parser parser);

}
