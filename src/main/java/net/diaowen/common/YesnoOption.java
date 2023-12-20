package net.diaowen.common;

/**
 * YesnoOption
 * @author keyuan(keyuan258@gmail.com)
 *枚举类型，包含了各种判断题选项。
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
public enum YesnoOption {

	OPTION1("是","否",0),
	OPTION2("对","错",1),
	OPTION3("正确","错误",2),
	OPTION4("同意","不同意",4),
	OPTION5("满意","不满意",5),
	OPTION6("喜欢","不喜欢",6),
	OPTION7("支持","反对", 7),
	OPTION8("Ture","False", 8),
	OPTION9("Yes","No",9);

	private String trueValue;
	private String falseValue;
	private int index;
	/**
	 * 构造函数
	 *
	 * @param trueValue 真值
	 * @param falseValue 假值
	 * @param index 索引
	 */
	YesnoOption(String trueValue,String falseValue,int index) {
		this.trueValue=trueValue;
		this.falseValue=falseValue;
		this.index=index;
	}
	/**
	 * 获取真值
	 *
	 * @return 真值
	 */
	public String getTrueValue() {
		return trueValue;
	}

	/**
	 * 获取假值
	 *
	 * @return 假值
	 */
	public String getFalseValue() {
		return falseValue;
	}

	/**
	 * 获取索引
	 *
	 * @return 索引
	 */
	public int getIndex() {
		return index;
	}
}
