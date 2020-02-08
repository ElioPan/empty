package com.ev.framework.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils{
	  /**
     * 不够位数的在前面补0，保留code的长度位数字
     * @param code
     * @return
     */
    public static String autoGenericCode(String code) {
        // 保留code的位数
        return String.format("%0" + code.length() + "d", Integer.parseInt(code) + 1);
    }

/**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        // 保留num的位数
    	// 0代表前面补充0
        // num 代表长度为4     
        // d 代表参数为正数型 
        return String.format("%0" + num + "d", Integer.parseInt(code) + 1);
    }

    /**
     * sql 模糊查询
     */
    public static String sqlLike(String column) {
        return  StringUtils.isEmpty(column)?column:new StringBuilder("%").append(column).append("%").toString();
    }


    public static String isOrder(String order) {
        if (order == null) {
            return "DESC";
        }
        String s = order.toUpperCase();
        if ("DESC".equals(s) || "ASC".equals(s)) {
            return order;
        }
        return "DESC";
    }
}
