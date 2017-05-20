package com.hbung.http.response;

import com.google.gson.annotations.SerializedName;
import com.hbung.json.GsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/15.
 */

public class HttpResponse {


    public transient String json;
    public transient int httpcode;

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("success")
    public boolean success;
    @SerializedName("error")
    public boolean error;

    @HttpCode.IHttpCode
    public int getCode() {
        return code;
    }

    //获取头部code
    public int getHttpcode() {
        return httpcode;
    }

    public String getMessage() {
        return message;
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
     * 方法功能：返回成功  success
     */

    public boolean isSucceed() {
        return success;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/2 11:06
     * <p>
     * 方法功能：接口失败  success
     */


    public boolean isError() {
        return error;
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/5/19 17:04
     * <p>
     * 方法功能：根据key获取对象
     */

    public <T> T getTypeToObject(Class<T> type, String... key) throws JSONException {
        if (json == null || key == null || key.length == 0) return null;
        JSONObject rootJsonOb = new JSONObject(json);
        for (String k : key) {
            rootJsonOb = rootJsonOb.getJSONObject(k);
        }
        T t = GsonHelper.getGson().fromJson(rootJsonOb.toString(), type);
        return t;
    }

}
