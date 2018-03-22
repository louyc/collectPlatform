package com.neusoft.np.arsf.db.infra.config;

public class DataSourceInfo {

	private static class DataSourceInfoHolder {
		private static final DataSourceInfo INSTANCE = new DataSourceInfo();
	}

	private DataSourceInfo() {
	}

	public static DataSourceInfo getInstance() {
		return DataSourceInfoHolder.INSTANCE;
	}
	
	

}
