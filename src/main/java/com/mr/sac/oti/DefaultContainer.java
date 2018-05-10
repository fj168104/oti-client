package com.mr.sac.oti;

import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.comm.ClientTransaction;
import com.mr.sac.oti.pack.JsonParser;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.HttpAgent;
import com.mr.sac.oti.protocal.ProtocolAgent;

import java.util.List;
import java.util.Map;

public class DefaultContainer extends BaseContainer {
	private static Parser JSON_PARSER = new JsonParser();
	private static ProtocolAgent HTTP_AGENT = new HttpAgent();

	private DefaultContainer() {
	}

	@Override
	public void loadRemoteConfiguration(String[] msgIds) {

	}

	@Override
	public void loadLocalConfiguration(String file) {

	}

	@Override
	public void initDataSource(String dsConfigId) {

	}

	public Transaction newTransaction(String requestMessageId, String responseMessageId) {
		return newTransaction(requestMessageId, responseMessageId, HTTP_AGENT, JSON_PARSER);
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent,
									  Parser parser) {
		Transaction transaction = new ClientTransaction(
				messageMap.get(requestMessageId),
				messageMap.get(requestMessageId));
		transaction.setParser(parser);
		transaction.setProtocolAgent(agent);
		return transaction;
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent) {
		return newTransaction(requestMessageId, responseMessageId, agent, JSON_PARSER);
	}

}
