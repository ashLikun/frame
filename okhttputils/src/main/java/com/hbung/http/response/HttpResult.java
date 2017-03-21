package com.hbung.http.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by likun
 * http返回的基本数据， 用泛型解耦，可以适用于大部分接口
 */
public class HttpResult<T> extends BaseHttpResponse {


    //用来模仿Data
    @SerializedName("list")
    public T data;

    public T getData() {
        json = null;
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code=" + code + "msg=" + msg);
        if (null != data) {
            sb.append(" subjects:" + data.toString());
        }
        return sb.toString();
    }
}
