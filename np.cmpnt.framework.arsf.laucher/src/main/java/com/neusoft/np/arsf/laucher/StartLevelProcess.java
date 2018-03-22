package com.neusoft.np.arsf.laucher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.startlevel.BundleStartLevel;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 启动bundle<br>
* 功能描述: 根据配置文件中内容，更改bundle启动级别，提供bundle启动操作<br>
* 创建日期: 2012-3-28 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-3-28       黄守凯        创建
* </pre>
 */
public class StartLevelProcess {

	/**
	 * 根据配置文件中内容，更改bundle启动级别
	 * 
	 * @param context
	 * @return
	 */
	protected static Map<String, Bundle> changeStartLevel(BundleContext context) {
		Bundle[] bundles = context.getBundles();
		Map<String, Bundle> bundleMap = new HashMap<String, Bundle>();
		Map<String, Integer> levelMap = PropertiesReader.getStartLevel();
		showProperties(levelMap);
		for (Bundle one : bundles) {
			String symbolicName = one.getSymbolicName();
			if (levelMap.containsKey(symbolicName)) {
				BundleStartLevel level = one.adapt(BundleStartLevel.class);
				level.setStartLevel(levelMap.get(symbolicName));
				bundleMap.put(symbolicName, one);
			}
		}
		return bundleMap;
	}

	private static void showProperties(Map<String, Integer> levelMap) {
		System.out.println("launcher.properties : ");
		Iterator<Entry<String, Integer>> lancherIter = levelMap.entrySet().iterator();
		while (lancherIter.hasNext()) {
			System.out.println(" - " + lancherIter.next());
		}
	}

	/**
	 * 调式输出
	 * 
	 * @param context
	 */
	protected static void showStartLevel(BundleContext context) {
		System.out.println("bundle start level : ");
		Bundle[] bundles = context.getBundles();
		for (Bundle one : bundles) {
			BundleStartLevel level = one.adapt(BundleStartLevel.class);
			System.out.println(" - " + one.getSymbolicName() + ":" + level.getStartLevel());
		}
	}

	/**
	 * 启动列表中所以bundle
	 * 
	 * @param bundleMap
	 * @throws BundleException
	 */
	protected static void startBundles(Map<String, Bundle> bundleMap) throws BundleException {
		Iterator<Entry<String, Bundle>> iter = bundleMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Bundle> entry = iter.next();
			entry.getValue().start();
		}
	}
	
	protected static void startBundlesByService(Map<String, Integer> levelMap) throws BundleException {
		Bundle[] bundles = Activator.getContext().getBundles();
		Map<String, Bundle> bundleMap = new HashMap<String, Bundle>();
		for (Bundle one : bundles) {
			String symbolicName = one.getSymbolicName();
			if (levelMap.containsKey(symbolicName)) {
				BundleStartLevel level = one.adapt(BundleStartLevel.class);
				level.setStartLevel(levelMap.get(symbolicName));
				bundleMap.put(symbolicName, one);
			}
		}
		startBundles(bundleMap);
	}

}
