package com.mr.sac.oti;

import com.mr.framework.db.ds.DSFactory;
import com.mr.framework.setting.Setting;
import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.comm.ClientTransaction;
import com.mr.sac.oti.pack.Parser;
import com.mr.sac.oti.protocal.ProtocolAgent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by feng on 18-5-9
 */
public abstract class BaseContainer extends OTIContainer {

	protected LinkedHashMap<String, Message> messageMap = new LinkedHashMap<>();

	@Override
	public void loadRemoteConfiguration(String[] msgIds) {

	}

	@Override
	public void loadLocalConfiguration(String file) {

	}

	@Override
	public synchronized void initDataSource(Setting setting) {
		if (Objects.isNull(dataSource)) {
			dataSource = DSFactory.create(setting).getDataSource();
		}
	}

	@Override
	public synchronized void initDataSource() {
		if (Objects.isNull(dataSource)) {
			dataSource = DSFactory.get();
		}
	}

	@Override
	public void addParam(Map<String, Object> params) {

	}

	@Override
	public Field newField(String messageId, String fieldTag) {
		return messageMap.get(messageId).getField(fieldTag).clone();
	}

	@Override
	public Message newMessage(String messageId) {
		return messageMap.get(messageId);
	}

	public Transaction newTransaction(String requestMessageId, String responseMessageId) {
		return null;
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent,
									  Parser parser) {
		return null;
	}

	@Override
	public Transaction newTransaction(String requestMessageId,
									  String responseMessageId,
									  ProtocolAgent agent) {
		return null;
	}

	public void initDataSource(String dsConfigId) {
	}

}
