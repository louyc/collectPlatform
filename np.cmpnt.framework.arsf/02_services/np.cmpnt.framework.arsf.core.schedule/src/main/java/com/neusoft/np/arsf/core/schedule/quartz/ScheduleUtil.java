package com.neusoft.np.arsf.core.schedule.quartz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台任务管理控制Bundle<br>
 * 功能描述: 工具类<br>
 * 创建日期: 2012-3-7 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-3-7       黄守凯        创建
 * </pre>
 */
public final class ScheduleUtil {

	private ScheduleUtil() {
	}

	/**
	 * Returns a date that is rounded to the next even second after the current time.
	 * 
	 * @return the new rounded date
	 */
	protected static Date evenSecondDateAfterNow() {
		return evenSecondDate(null);
	}

	/**
	 * <p>
	 * Returns a date that is rounded to the next even second above the given
	 * date.
	 * </p>
	 * 
	 * @param date
	 *          the Date to round, if <code>null</code> the current time will
	 *          be used
	 * @return the new rounded date
	 */
	protected static Date evenSecondDate(Date date) {
		Calendar c = Calendar.getInstance();
		if (date == null) {
			c.setTime(new Date());
		} else {
			c.setTime(date);
		}
		c.setLenient(true);
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 1);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * <p>
	 * Returns a date that is rounded to the next even minute after the current time.
	 * </p>
	 * 
	 * <p>
	 * For example a current time of 08:13:54 would result in a date
	 * with the time of 08:14:00. If the date's time is in the 59th minute,
	 * then the hour (and possibly the day) will be promoted.
	 * </p>
	 * 
	 * @return the new rounded date
	 */
	protected static Date evenMinuteDateAfterNow() {
		return evenMinuteDate(null);
	}

	/**
	 * <p>
	 * Returns a date that is rounded to the next even minute above the given
	 * date.
	 * </p>
	 * 
	 * <p>
	 * For example an input date with a time of 08:13:54 would result in a date
	 * with the time of 08:14:00. If the date's time is in the 59th minute,
	 * then the hour (and possibly the day) will be promoted.
	 * </p>
	 * 
	 * @param date
	 *          the Date to round, if <code>null</code> the current time will
	 *          be used
	 * @return the new rounded date
	 */
	protected static Date evenMinuteDate(Date date) {
		Calendar c = Calendar.getInstance();
		if (date == null) {
			c.setTime(new Date());
		} else {
			c.setTime(date);
		}
		c.setLenient(true);
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 字符串验证，针对key验证；
	 */
	protected static boolean checkString(String str) {
		if (str == null) {
			return false;
		}
		if ("".equals(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证多个String类型字符串是否为null或者""
	 */
	protected static boolean checkArgument(String... argument) {
		List<String> arguments = java.util.Arrays.asList(argument);
		for (String elem : arguments) {
			if (elem == null) {
				return false;
			}
			if ("".equals(elem)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取项目的文件的输入流，如果项目在jar中，则先在jar中查找文件，找不到再到jar外面查找文件
	 */
	@SuppressWarnings("resource")
	protected static InputStream getProjectFile(String filePath) throws IOException {
		InputStream inputStream = null;
		String currentJarPath = URLDecoder.decode(ScheduleUtil.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile(), "UTF-8"); // 获取当前Jar文件名
		String path = ScheduleUtil.class.getResource("").getPath();
		if (path.indexOf('!') != -1) {// 在jar中
			JarFile currentJar = new JarFile(currentJarPath);
			JarEntry dbEntry = currentJar.getJarEntry(filePath);
			if (dbEntry != null) {
				inputStream = currentJar.getInputStream(dbEntry);
			}
		}
		if (inputStream == null) {
			String loaderPath = currentJarPath;
			String packPath = ScheduleUtil.class.getPackage().toString();
			packPath = packPath.replace("package ", "");
			packPath = "/" + packPath.replace(".", "/");
			if (!loaderPath.endsWith("/")) {
				loaderPath = loaderPath.substring(0, loaderPath.lastIndexOf('/')) + "/";
			}
			loaderPath = loaderPath.replace(packPath, "");
			// 适应OSGi路径环境修改 ：bin
			String tPath = loaderPath + filePath;
			inputStream = new FileInputStream(tPath);
		}
		return inputStream;
	}
}
