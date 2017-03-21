package com.hbung.http;


import com.hbung.json.GsonHelper;
import com.hbung.http.response.BaseHttpResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/24　16:36
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class HttpSubscription<ResultType> extends Subscriber<Response> {

    private Callback<ResultType> callBack;


    public HttpSubscription(Callback<ResultType> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onStart() {
        if (callBack != null) {
            callBack.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }


    @Override
    public void onNext(Response response) {
        try {
            if (callBack != null) {
                try {
                    Type type = getType();
                    if (type == Response.class) {
                        callBack.onSuccess((ResultType) response);
                    }
                    if (type == ResponseBody.class) {
                        callBack.onSuccess((ResultType) response.body());
                    } else if (type == String.class) {
                        callBack.onSuccess((ResultType) response.body().string());
                    } else {
                        String json = response.body().string();
                        ResultType res = GsonHelper.getGson().fromJson(json, type);
                        if (res instanceof BaseHttpResponse) {
                            ((BaseHttpResponse) res).json = json;
                            ((BaseHttpResponse) res).httpcode = response.code();
                        }
                        callBack.onSuccess(res);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(new Exception(HttpSubscription.this.toString() + "解析数据错误", e));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }
    }


    /**
     * 获取回调里面的泛型
     */
    private Type getType() {
        Type types = callBack.getClass().getGenericSuperclass();
        if (types == null) {
            new Throwable("HttpSubscription  ->>>  未获取到callBack的超级类");
        }
        List<Type> needtypes = new ArrayList<>();
        if (types instanceof ParameterizedType) {
            Type[] parentypes = ((ParameterizedType) types).getActualTypeArguments();
            if (parentypes == null || parentypes.length == 0) {
                new Throwable("HttpSubscription  ->>>  callBack回调 不能没有泛型，请查看HttpCallBack是否有泛型");
            }
            for (Type childtype : parentypes) {
                needtypes.add(childtype);
                if (childtype instanceof ParameterizedType) {
                    Type[] childtypes = ((ParameterizedType) childtype).getActualTypeArguments();
                    for (Type type : childtypes) {
                        needtypes.add(type);
                    }
                }
            }
        } else {
            new Throwable("HttpSubscription  ->>>  callBack回调 不能没有泛型，请查看HttpCallBack是否有泛型");
        }
        if (needtypes.size() > 0)
            return needtypes.get(0);
        else {
            new Throwable("HttpSubscription  ->>>  callBack回调 不能没有泛型，请查看HttpCallBack是否有泛型");
            return null;
        }
    }
}
