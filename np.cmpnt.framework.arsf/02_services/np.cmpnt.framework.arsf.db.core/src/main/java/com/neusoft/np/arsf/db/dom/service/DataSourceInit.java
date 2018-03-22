package com.neusoft.np.arsf.db.dom.service;

import java.util.Map;

import com.neusoft.np.arsf.db.dom.pool.DataSourceItem;
import com.neusoft.np.arsf.db.dom.pool.DataSourceItemPool;

public class DataSourceInit {

	private DataSourceItemPool pool;

	private Map<String, Map<String, Map<String, String>>> pmap;

	private Map<String, DataSourceItem> dcMap;

	public void init() throws DataSourceInitException {
		pool = DataSourceItemPool.getInstance();
		DataSourceInitProp prop = new DataSourceInitProp();
		pmap = prop.initProperties();

		DataSourceInitItem item = new DataSourceInitItem();
		dcMap = item.initDataSource(pmap);

		DataSourceInitFactory factory = new DataSourceInitFactory();
		factory.initDataFactory(dcMap);

		pool.setDcMap(dcMap);
	}

}
