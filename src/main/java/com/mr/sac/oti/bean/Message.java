package com.mr.sac.oti.bean;

import com.mr.sac.oti.Node;

import java.util.ArrayList;
import java.util.List;

import com.mr.framework.core.clone.Cloneable;
import com.mr.sac.oti.pack.Parser;

/**
 * Created by feng on 18-5-6
 */
public class Message implements Node, Cloneable {
	private List<Field> fields = new ArrayList<Field>();

	public void addNode(Node node) {
		if (node instanceof Field) {
			fields.add((Field) node);
		} else {
			throw new RuntimeException("Message 中 Node 类型必须为Field");
		}
	}

	public void pack(Parser parser) {
		parser.packMessage(this);
	}

	public void unpack(String receivedData, Parser parser) {
		parser.unpackMessage(receivedData);
	}

	public Message clone() {
		return null;
	}
}
