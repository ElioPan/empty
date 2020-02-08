package com.ev.common.domain;

/**
 * 统计用bean类
 * 
 * 
 */
public class BeanDO {
	// 内容
	private String keyStr;
	// 数值
	private String valueStr;

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

	@Override
	public String toString() {
		return "BeanDO{" +
				"keyStr='" + keyStr + '\'' +
				", valueStr='" + valueStr + '\'' +
				'}';
	}
}
