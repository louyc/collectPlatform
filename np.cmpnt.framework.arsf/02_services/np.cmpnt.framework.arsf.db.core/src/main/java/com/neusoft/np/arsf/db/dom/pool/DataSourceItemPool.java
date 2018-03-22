package com.neusoft.np.arsf.db.dom.pool;

import java.util.Map;

import com.neusoft.np.arsf.db.infra.constants.DBConstants;

public class DataSourceItemPool {

	private static class DataSourceItemPoolHolder {
		private static final DataSourceItemPool INSTANCE = new DataSourceItemPool();
	}

	private DataSourceItemPool() {
	}

	public static DataSourceItemPool getInstance() {
		return DataSourceItemPoolHolder.INSTANCE;
	}

	private Map<String, DataSourceItem> dcMap;

	public Map<String, DataSourceItem> getDcMap() {
		return dcMap;
	}

	public void setDcMap(Map<String, DataSourceItem> dcMap) {
		this.dcMap = dcMap;
	}

	public DataSourceItem getDefault() {
		if (dcMap != null) {
			return dcMap.get(DBConstants.DEFAULT_SOURCE);
		}
		int i = 5;
		while (i > 0) {
			if (dcMap != null) {
				return dcMap.get(DBConstants.DEFAULT_SOURCE);
			}
			sleep(i);
			i--;
		}
		return null;
	}

	private void sleep(int second) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
