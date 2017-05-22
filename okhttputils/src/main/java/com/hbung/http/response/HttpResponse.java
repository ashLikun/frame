package com.hbung.http.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.hbung.json.GsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/15.
 */

public class HttpResponse {
    public transient Response response;
    public static final String CACHE_ROOT = "root";//缓存的跟节点key

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
        JSONObject js = getCacheJsonObject(CACHE_ROOT, json);
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
        if (json == null) return null;

        String resStr = json;
        if (key != null) {
            for (String k : key) {
                resStr = getKeyToValue(resStr, k);
            }
        }
        if (TextUtils.isEmpty(resStr)) {
            return null;
        }
        T t = GsonHelper.getGson().fromJson(resStr, type);
        return t;
    }

    private String getKeyToValue(String root, String key) throws JSONException {
        if (!TextUtils.isEmpty(root)) {
            try {

                Object jsonObject = getCacheJSON(key,root);
                if(jsonObject != null){
                    if(jsonObject instanceof JSONObject){
                        root = jsonObject.get(key).toString();
                    }
                }

            } catch (JSONException e) {
                //尝试用数组方式解析
                JSONArray jsonArray = new JSONArray(root);
                if (jsonArray != null && jsonArray.length() > 0) {
                    root = jsonArray.getJSONObject(0).get(key).toString();
                }
            }
        }
        return root;
    }

    private Object getCacheJSON(String key, String content) throws JSONException {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) return null;
        if (cache == null) {
            cache = new HashMap<>();
        }
        Object o = cache.get(key);
        if (o == null) {
            try {
                o = new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
                o = new JSONArray(content);
            }
            cache.put(key, o);
        }
        return o;
    }


    //缓存已经实例化的JSONObject,JSONArray对象
    private HashMap<String, Object> cache;

}
