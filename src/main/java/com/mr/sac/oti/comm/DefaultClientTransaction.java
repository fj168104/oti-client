package com.mr.sac.oti.comm;

import com.mr.sac.oti.pack.Parser;

/**
 * Created by feng on 18-5-6
 */
public class DefaultClientTransaction extends ClientTransaction {

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
