package com.mr.sac.oti;

import com.mr.sac.oti.bean.Field;
import com.mr.sac.oti.bean.Message;

import java.util.List;
import java.util.Map;

public class DefaultContainer extends BaseContainer {

	private DefaultContainer() {
	}

	@Override
	public void loadRemoteConfiguration(String... msgIds) {

	}

	@Override
	public void loadLocalConfiguration(String file) {

	}

	@Override
	public void initDataSource(String dsConfigId) {

	}

	@Override
	public Field newField(String fieldTag) {
		return null;
	}

	@Override
	public Message newMessage(String messageId) {
		return null;
	}

	@Override
	public Transaction newTransaction(List<Message> messages) {
		return null;
	}

	@Override
	public void addParam(Map<String, Object> params) {

	}
}
