/* 
 * @(#)StringHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.hbung.utils.other;

import android.text.TextUtils;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 13:53
 * <p>
 * 方法功能：字符串一些工具类
 */

public class StringUtils {


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 14:08
     * <p>
     * 方法功能：去除字符串空字符
     */
    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 是否绝对的空
     *
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0 || str.equals("null"));
    }

    /**
     * 是否正常的字符串
     *
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /*
     * 比较两个字符串
     */
    public static boolean isEquals(String actual, String expected) {
        return actual == expected
                || (actual == null ? expected == null : actual.equals(expected));
    }


    /**
     * bytes[]转换成Hex字符串,可用于URL转换，IP地址转换
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 字节转换成合适的单位
     *
     * @param value
     * @return
     */
    public static String prettyBytes(long value) {
        String args[] = {"B", "KB", "MB", "GB", "TB"};
        StringBuilder sb = new StringBuilder();
        int i;
        if (value < 1024L) {
            sb.append(String.valueOf(value));
            i = 0;
        } else if (value < 1048576L) {
            sb.append(String.format("%.1f", value / 1024.0));
            i = 1;
        } else if (value < 1073741824L) {
            sb.append(String.format("%.2f", value / 1048576.0));
            i = 2;
        } else if (value < 1099511627776L) {
            sb.append(String.format("%.3f", value / 1073741824.0));
            i = 3;
        } else {
            sb.append(String.format("%.4f", value / 1099511627776.0));
            i = 4;
        }
        sb.append(' ');
        sb.append(args[i]);
        return sb.toString();
    }


    /**
     * 判断View 的Text是否为空
     */
    public static boolean isEmpty(TextView textView) {
        if (textView != null && !TextUtils.isEmpty(textView.getText().toString().trim())) {
            return false;
        }
        return true;
    }


    /**
     * 非空判断处理和转换为String类型
     * dataFilter("aaa")  -> aaa
     * dataFilter(null)    ->"未知"
     * dataFilter("aaa","未知")  -> aaa
     * <p>
     * dataFilter(123.456  ,  2) -> 123.46
     * dataFilter(123.456  ,  0) -> 123
     * dataFilter(123.456  )    -> 123.46
     * <p>
     * dataFilter(56  )    -> "56"
     * <p>
     * dataFilter(true)        ->true
     *
     * @param source 主要对String,Integer,Double这三种类型进行处理
     * @param filter 要改的内容，这个要转换的内容可以不传，
     *               1如传的是String类型就会认为String为空时要转换的内容，不传为空时默认转换为未知，
     *               2如果传入的是intent类型，会认为double类型要保留的小数位数，
     *               3如是传入的是0会认为double要取整
     * @return 把内容转换为String返回
     */
    public static String dataFilter(Object source, Object filter) {
        try {
            if (source != null && !isBlank(source.toString())) {//数据源没有异常
                if (source instanceof String) {//String 处理
                    return source.toString().trim();
                } else if (source instanceof Double ||source instanceof Float) {//小数处理，
                    BigDecimal bd = new BigDecimal(Double.parseDouble(source.toString()));
                    if (filter != null && filter instanceof Integer) {
                        if ((int) filter == 0) {
                            return String.valueOf((int) (bd.setScale(0, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
                        } else {

                            return String.valueOf(bd.setScale(Math.abs((int) filter), BigDecimal.ROUND_HALF_EVEN).doubleValue());
                        }
                    }
                    return String.valueOf(bd.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
                } else if (source instanceof Integer || source instanceof Boolean) {
                    return source.toString();
                } else {
                    return "未知";
                }
            } else if (filter != null) {//数据源异常 并且filter不为空
                return filter.toString();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return "未知";
    }

    public static String dataFilter(Object source) {
        return dataFilter(source, null);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 14:07
     * <p>
     * 方法功能：返回指定长度的字符串
     */
    public static String isNullToConvert(String str, int maxLenght) {
        return isBlank(str) ? "未知" : str.substring(0, str.length() < maxLenght ? str.length() : maxLenght);

    }

    /**
     * 小数 四舍五入 19.0->19.0    返回Double
     */
    public static Double roundDouble(double val, int precision) {
        int resQ = (int) Math.round(val * Math.pow(10.0, precision));
        double res = resQ / Math.pow(10.0, precision);
        return res;
    }

    /**
     * 小数后两位
     *
     * @param val
     * @return
     */
    public static Double roundDouble(double val) {
        int resQ = (int) Math.round(val * Math.pow(10.0, 2));
        double res = resQ / Math.pow(10.0, 2);
        return res;
    }

    /**
     * 小数 四舍五入 19.0->19.0   返回字符串
     */

    public static String roundString(double val, int precision) {
        return String.valueOf(roundDouble(val, precision));

    }


    public static String getUrlToFileName(String url) {
        String res = null;
        if (url != null) {
            String[] ress = url.split("/");
            if (ress.length > 0) {
                res = ress[ress.length - 1];
            }
        }
        return res;
    }


    public static String getMoney(int money) {
        return money <= 0 ? "- -" : String.valueOf(money);
    }

    public static String getMessageCount(int count) {
        if (count > 0) {
            if (count >= 100) {
                return "99+";
            } else {
                return String.valueOf(count);
            }
        } else {
            return String.valueOf("");
        }
    }


}
