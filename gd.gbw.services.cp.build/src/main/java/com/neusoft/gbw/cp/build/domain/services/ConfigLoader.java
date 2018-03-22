package com.neusoft.gbw.cp.build.domain.services;

import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class ConfigLoader {

	public void loadConfig() {
		//获取配置文件，配置文件所在位置com.neusoft.np.arsf.core.config工程下的config包配置文件，自己新建配置文件，然后再fileListConfig文件中将配置文件写入
		Map<String, String> configMap = ARSFToolkit.getConfiguration().getAllBusinessProperty("gbw_business");
		try {
			NMBeanUtils.fillClassStaticFields(ConfigVariable.class, configMap);
		} catch (NMBeanUtilsException e) {
			Log.error("初始化配置文件错误", e);
		}
	}
}
