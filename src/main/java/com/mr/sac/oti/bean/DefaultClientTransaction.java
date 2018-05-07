package com.mr.sac.oti.bean;

import com.mr.sac.oti.listen.Listener;
import com.mr.sac.oti.pack.Parser;

import java.util.List;

/**
 * Created by feng on 18-5-6
 */
public class DefaultClientTransaction extends ClientTransaction{

	private Parser parser;

	protected String serializeRequestMessages() {
		requestMessage.pack(parser);
		return parser.outputPackedString();
	}

	@Override
	protected void deSerializeResponseMessages(String responseString) {
		responseMessage.unpack(responseString, parser);
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}
}
