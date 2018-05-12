package com.mr.sac.oti;

import com.mr.sac.oti.comm.ClientTransaction;
import com.mr.sac.oti.pack.JsonParser;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.HttpAgent;
import com.mr.sac.oti.protocal.ProtocolAgent;

public class DefaultContainer extends BaseContainer {
	private static Parser JSON_PARSER = new JsonParser();
	private static ProtocolAgent HTTP_AGENT = new HttpAgent();

	private DefaultContainer() {
	}

	public Transaction newTransaction(String requestMessageId, String responseMessageId) {
		return newTransaction(requestMessageId, responseMessageId, HTTP_AGENT, JSON_PARSER);
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent) {
		return newTransaction(requestMessageId, responseMessageId, agent, JSON_PARSER);
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent,
									  Parser parser) {
		checkTransactionAble(requestMessageId, responseMessageId);
		Transaction transaction = new ClientTransaction(
				newMessage(requestMessageId),
				newMessage(responseMessageId));
		transaction.setParser(parser);
		transaction.setProtocolAgent(agent);
		return transaction;
	}
}
