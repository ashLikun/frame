package com.hbung.http.mode;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/15.
 */

public class BaseHttpResponse {


    public transient String json;

    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;


    public int getCode() {
        return code;
    }

    public HttpCode getHttpCode() {
        return HttpCode.getState(code);
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getJSONObject() throws JSONException {
        JSONObject js = new JSONObject(json);
        json = null;
        return js;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/2 11:05
     * <p>
     * 方法功能：返回成功  0
     */

    public boolean isSucceed() {
        return HttpCode.getState(code) == HttpCode.SUCCEED;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/2 11:06
     * <p>
     * 方法功能：接口失败  1
     */


    public boolean isFaiure() {
        return HttpCode.getState(code) == HttpCode.FAILURE;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/2 11:07
     * <p>
     * 方法功能：返回失败 2
     */


    public boolean isReturnFaiure() {
        return HttpCode.getState(code) == HttpCode.RETURN_FAILURE;
    }
}
