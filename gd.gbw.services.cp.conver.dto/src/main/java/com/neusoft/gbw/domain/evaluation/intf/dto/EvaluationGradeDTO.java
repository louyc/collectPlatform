package com.neusoft.gbw.domain.evaluation.intf.dto;

import com.neusoft.gbw.infrastructure.base.BaseDTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class EvaluationGradeDTO extends BaseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170445387199730264L;

	@XStreamAsAttribute
	@XStreamAlias("tableName")
	private String tableName;  //打分任务的表名
	
	@XStreamAsAttribute
	@XStreamAlias("scoringId")
	private String scoringId;  //打分ID，如果为空，则是对整个表数据进行打分
	
	@XStreamAsAttribute
	@XStreamAlias("returnType")
	private String returnType; //打分返回值类型  0:成功  1:失败
	
	@XStreamAsAttribute
	@XStreamAlias("returnDesc")
	private String returnDesc; //打分描述   成功返回成功；失败返回智能评估失败信息或者异常信息
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getScoringId() {
		return scoringId;
	}
	public void setScoringId(String scoringId) {
		this.scoringId = scoringId;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getReturnDesc() {
		return returnDesc;
	}
	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}
}
