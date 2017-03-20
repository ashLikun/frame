package com.hbung.http.mode;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/2　11:01
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：http的返回值
 */

public enum HttpCode {


    SUCCEED(0, "成功"), FAILURE(-1, "接口失败"), RETURN_FAILURE(-2, "返回失败"), DISABLE(-200, "禁用"), WEIZHI(-100, "未知");

    private String valuse;
    private int key;

    HttpCode(int key, String valuse) {
        this.valuse = valuse;
        this.key = key;
    }

    public String getValuse() {
        return valuse;
    }

    public int getKey() {
        return key;
    }

    public static HttpCode getState(int status) {
        if (status == HttpCode.SUCCEED.getKey()) {
            return SUCCEED;
        } else if (status == HttpCode.FAILURE.getKey()) {
            return FAILURE;
        } else if (status == HttpCode.RETURN_FAILURE.getKey()) {
            return RETURN_FAILURE;
        } else if (status == HttpCode.DISABLE.getKey()) {
            return DISABLE;
        } else if (status == HttpCode.WEIZHI.getKey()) {
            return WEIZHI;
        }
        return WEIZHI;
    }
}
