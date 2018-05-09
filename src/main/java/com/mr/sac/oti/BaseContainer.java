package com.mr.sac.oti;

import com.mr.framework.db.ds.DSFactory;
import com.mr.framework.setting.Setting;

import java.util.Objects;

/**
 * Created by feng on 18-5-9
 */
public abstract class BaseContainer extends OTIContainer {

	@Override
	public synchronized void initDataSource(Setting setting) {
		if (Objects.isNull(dataSource)) {
			dataSource = DSFactory.create(setting).getDataSource();
		}
	}

	public synchronized void initDataSource() {
		if (Objects.isNull(dataSource)) {
			dataSource = DSFactory.get();
		}

	}
}
