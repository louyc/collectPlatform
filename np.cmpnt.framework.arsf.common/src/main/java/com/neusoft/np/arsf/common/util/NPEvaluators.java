package com.neusoft.np.arsf.common.util;

import com.neusoft.nms.util.evaluate.expression.AbstractExpression;
import com.neusoft.nms.util.evaluate.expression.Expression;
import com.neusoft.nms.util.evaluate.function.AbstractFunction;
import com.neusoft.nms.util.evaluate.function.Function;
import com.neusoft.nms.util.evaluate.function.FunctionEnum;
import com.neusoft.nms.util.evaluate.operator.AbstractOperator;
import com.neusoft.nms.util.evaluate.operator.Operator;
import com.neusoft.nms.util.evaluate.operator.OperatorEnum;

/**
* 项目名称: 采集平台框架<br>
* 模块名称: 数据运算组件<br>
* 功能描述: 数据运算组件以静态方法形式对外提供功能的接口；<br>
* 		此类包括了Operator、Function、Expression所支持的全部功能；<br>
* 		通过此类使用数据运算组件类似于使用通常的工具类。<br>
* 创建日期: 2012-5-14 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-5-14      马仲佳        创建
* </pre>
*/
public class NPEvaluators {

	private NPEvaluators() {
	}

	// ======================operator======================
	/**
	 * 加法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：double
	 * @param secondOperand
	 *            第二个操作数，操作数类型：double
	 * @return 两个操作数相加的值，返回类型：double
	 */
	public static double add(double firstOperand, double secondOperand) {
		Operator operator = getOperator(OperatorEnum.ADDITION);
		return operator.operator(firstOperand, secondOperand);
	}

	/**
	 * 加法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：String
	 * @param secondOperand
	 *            第二个操作数，操作数类型：String
	 * @return 两个操作数相加的值，返回类型：String
	 */
	public static String add(String firstOperand, String secondOperand) {
		Operator operator = getOperator(OperatorEnum.ADDITION);
		return operator.operator(firstOperand, secondOperand);
	}

	/**
	 * 减法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：double
	 * @param secondOperand
	 *            第二个操作数，操作数类型：double
	 * @return 第一个操作数减去第二个操作数的值，返回类型：double
	 */
	public static double subtract(double firstOperand, double secondOperand) {
		Operator operator = getOperator(OperatorEnum.SUBTRACTION);
		return operator.operator(firstOperand, secondOperand);
	}

	/**
	 * 减法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：String
	 * @param secondOperand
	 *            第二个操作数，操作数类型：String
	 * @return 第一个操作数减去第二个操作数，返回类型：String
	 */
	public static String subtract(String firstOperand, String secondOperand) {
		Operator operator = getOperator(OperatorEnum.SUBTRACTION);
		return operator.operator(firstOperand, secondOperand);
	}

	/**
	 * 乘法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：double
	 * @param secondOperand
	 *            第二个操作数，操作数类型：double
	 * @return 第一个操作数乘以第二个操作数的值，返回类型：double
	 */
	public static double multiply(double firstOperand, double secondOperand) {
		Operator operator = getOperator(OperatorEnum.MULTIPLICATION);
		return operator.operator(firstOperand, secondOperand);
	}

	/**
	 * 乘法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：String
	 * @param secondOperand
	 *            第二个操作数，操作数类型：String
	 * @return 第一个操作数乘以第二个操作数，返回类型：String
	 */
	public static String multiply(String firstOperand, String secondOperand) {
		Operator operator = getOperator(OperatorEnum.MULTIPLICATION);
		return operator.operator(firstOperand, secondOperand);
	}

	/**
	 * 除法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：double
	 * @param secondOperand
	 *            第二个操作数，操作数类型：double
	 * @param scale
	 *            精度
	 * @return 第一个操作数除以第二个操作数的值，返回类型：double
	 */
	public static double divide(double firstOperand, double secondOperand, int scale) {
		Operator operator = getOperator(OperatorEnum.DIVISION);
		return operator.operator(firstOperand, secondOperand, scale);
	}

	/**
	 * 除法运算
	 * 
	 * @param firstOperand
	 *            第一个操作数，操作数类型：String
	 * @param secondOperand
	 *            第二个操作数，操作数类型：String
	 * @param scale
	 *            精度
	 * @return 第一个操作数除以第二个操作数，返回类型：String
	 */
	public static String divide(String firstOperand, String secondOperand, int scale) {
		Operator operator = getOperator(OperatorEnum.DIVISION);
		return operator.operator(firstOperand, secondOperand, scale);
	}

	// ======================function======================
	/**
	 * 求和运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：double
	 * @return 操作数组进行求和运算后结果，类型：double
	 */
	public static double sum(double[] operands) {
		Function function = getFunction(FunctionEnum.SUM);
		return function.function(operands);
	}

	/**
	 * 求和运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：String
	 * @return 操作数组进行求和运算后结果，类型：String
	 */
	public static String sum(String[] operands) {
		Function function = getFunction(FunctionEnum.SUM);
		return function.function(operands);
	}

	/**
	 * 求最大值运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：double
	 * @return 操作数组进行最大值运算后结果，类型：double
	 */
	public static double max(double[] operands) {
		Function function = getFunction(FunctionEnum.MAX);
		return function.function(operands);
	}

	/**
	 * 求最大值运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：String
	 * @return 操作数组进行最大值运算后结果，类型：String
	 */
	public static String max(String[] operands) {
		Function function = getFunction(FunctionEnum.MAX);
		return function.function(operands);
	}

	/**
	 * 求最小值运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：double
	 * @return 操作数组进行最小值运算后结果，类型：double
	 */
	public static double min(double[] operands) {
		Function function = getFunction(FunctionEnum.MIN);
		return function.function(operands);
	}

	/**
	 * 求最小值运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：String
	 * @return 操作数组进行最小值运算后结果，类型：String
	 */
	public static String min(String[] operands) {
		Function function = getFunction(FunctionEnum.MIN);
		return function.function(operands);
	}

	/**
	 * 求最平均值运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：double
	 * @param scale
	 *            精度
	 * @return 操作数组进行平均值运算后结果，类型：double
	 */
	public static double average(double[] operands, int scale) {
		Function function = getFunction(FunctionEnum.AVERAGE);
		return function.function(operands, scale);
	}

	/**
	 * 求平均值运算
	 * 
	 * @param operands
	 *            操作数组，操作数类型：String
	 * @param scale
	 *            精度
	 * @return 操作数组进行平均值运算后结果，类型：String
	 */
	public static String average(String[] operands, int scale) {
		Function function = getFunction(FunctionEnum.AVERAGE);
		return function.function(operands, scale);
	}

	// ======================expression======================
	/**
	 * 求表达式值运算
	 * 
	 * @param exp
	 *            表达式，类型：String
	 * @return 表达式运算结果
	 */
	public static double express(String exp) {
		Expression expression = getExpression();
		return expression.express(exp);
	}

	/**
	 * 求表达式值运算
	 * 
	 * @param exp
	 *            表达式，类型：String
	 * @param scale
	 *            精度
	 * @return 表达式运算结果
	 */
	public static double express(String exp, int scale) {
		Expression expression = getExpression();
		return expression.express(exp, scale);
	}

	// ======================私有方法======================
	/**
	 * 获取操作符对象
	 * 
	 * @param operatorEnum
	 * @return
	 */
	private static Operator getOperator(OperatorEnum operatorEnum) {
		return AbstractOperator.getInstance(operatorEnum);
	}

	/**
	 * 获取函数符对象
	 * 
	 * @param sum
	 * @return
	 */
	private static Function getFunction(FunctionEnum functionEnum) {
		return AbstractFunction.getInstance(functionEnum);
	}

	/**
	 * 获取表达式对象
	 * 
	 * @return
	 */
	private static Expression getExpression() {
		return AbstractExpression.getInstance();
	}
}