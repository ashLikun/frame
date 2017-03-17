package com.hbung.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbung.http.httpquest.JsonTypeAdapter;

/**
 * Created by Administrator on 2016/8/4.
 * gson的默认值处理
 * gson的注解
 *
 * @Expose 是否序列化与反序列化
 * @SerializedName 序列化时候的名称
 */

public class GsonHelper {


    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(int.class, new JsonTypeAdapter.IntegerTypeAdapter())
                .registerTypeAdapter(long.class, new JsonTypeAdapter.LongTypeAdapter())
                .registerTypeAdapter(float.class, new JsonTypeAdapter.FloatTypeAdapter())
                .registerTypeAdapter(double.class, new JsonTypeAdapter.DoubleTypeAdapter())
                .serializeNulls()
                .create();
    }


}
