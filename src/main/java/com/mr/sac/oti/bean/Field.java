package com.mr.sac.oti.bean;

import com.mr.framework.core.clone.Cloneable;
import com.mr.sac.oti.Node;
import com.mr.sac.oti.pack.Parser;

import java.util.List;

/**
 * Created by feng on 18-5-6
 */
public class Field implements Node, Cloneable {

	private List<Message> arrayMessage;

	private Message objectMessage;

	public void addNode(Node node) {
	}

	public void pack(Parser parser) {
		parser.packField(this);
	}

	public void unpack(String receivedData, Parser parser) {

	}

	public Field clone() {
		return null;
	}
}
