package com.neusoft.np.arsf.service.transfer;

/**
 * 
 * 项目名称: 处理平台<br>
 * 模块名称: 处理平台核心服务<br>
 * 功能描述: 发送对象<br>
 * 创建日期: 2012-12-20 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-20       刘勃宏        创建 
 * </pre>
 */
public class SendInfo {

	public String sendType;

	public String resultData;

	public SendInfo() {
	}

	public SendInfo(String sendType, String resultData) {
		this.sendType = sendType;
		this.resultData = resultData;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getResultData() {
		return resultData;
	}

	public void setResultData(String resultData) {
		this.resultData = resultData;
	}

	@Override
	public String toString() {
		return "SendInfo [sendType=" + sendType + ", resultData=" + resultData + "]";
	}

}
