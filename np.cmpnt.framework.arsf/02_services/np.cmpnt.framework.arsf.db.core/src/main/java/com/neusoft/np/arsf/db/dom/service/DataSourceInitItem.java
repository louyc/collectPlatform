package com.neusoft.np.arsf.db.dom.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.neusoft.np.arsf.db.dom.pool.DataSourceItem;
import com.neusoft.np.arsf.db.infra.constants.DBConstants;

public class DataSourceInitItem {

	// 初始化DataSource
	private Map<String, DataSourceItem> dcMap = new HashMap<String, DataSourceItem>();

	protected Map<String, DataSourceItem> initDataSource(Map<String, Map<String, Map<String, String>>> pmap)
			throws DataSourceInitException {
		Map<String, Map<String, String>> datasource = pmap.get(DBConstants.DATASOURCE_PRE);
		processDataSource(datasource);
		Map<String, Map<String, String>> c3p0 = pmap.get(DBConstants.C3P0_PRE);
		processC3P0(c3p0);
		return dcMap;
	}

	private void processDataSource(Map<String, Map<String, String>> datasource) {
		Iterator<Entry<String, Map<String, String>>> datasourceIter = datasource.entrySet().iterator();
		while (datasourceIter.hasNext()) {
			Entry<String, Map<String, String>> e = datasourceIter.next();
			String url = e.getValue().get(DBConstants.URL);
			String driverClassName = e.getValue().get(DBConstants.DRIVER);
			String username = e.getValue().get(DBConstants.USERNAME);
			String password = e.getValue().get(DBConstants.PSWORD);
			DataSourceItem item = new DataSourceItem(url, driverClassName, username, password);
			dcMap.put(e.getKey(), item);
		}
	}

	private void processC3P0(Map<String, Map<String, String>> cmap) {
		Iterator<Entry<String, Map<String, String>>> datasourceIter = cmap.entrySet().iterator();
		while (datasourceIter.hasNext()) {
			Entry<String, Map<String, String>> e = datasourceIter.next();
			DataSourceItem item = dcMap.get(e.getKey());
			if (item == null) {
				continue;
			}
			item.setC3p0Config(e.getValue());
		}
	}
}
