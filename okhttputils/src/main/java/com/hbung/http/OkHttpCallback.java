package com.hbung.http;

import com.hbung.http.response.BaseHttpResponse;
import com.hbung.http.response.ResponseSimeple;
import com.hbung.json.GsonHelper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/23　14:14
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

class OkHttpCallback<ResultType> implements okhttp3.Callback {
    ExecuteCall exc;
    Callback<ResultType> callback;
    MainThread mMainThread;

    public OkHttpCallback(MainThread mMainThread, ExecuteCall exc, Callback<ResultType> callback) {
        this.exc = exc;
        this.callback = callback;
        this.mMainThread = mMainThread;
        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        postFailure(e);
    }

    private void postFailure(final Throwable throwable) {
        mMainThread.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(throwable);
                exc.setCompleted(true);
                callback.onCompleted();
            }
        });
    }

    private void postResponse(final Response response, final ResultType resultType) {
        mMainThread.execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(resultType);
                exc.setCompleted(true);
                callback.onCompleted();
                response.body().close();
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            try {
                ResultType resultType = handerResult(call, response);
                postResponse(response, resultType);
            } catch (IOException e) {
                e.printStackTrace();
                onFailure(call, e);
                response.close();
            }
        } else {
            postFailure(new HttpException(response));
            response.close();
        }

    }

    private ResultType handerResult(Call call, final Response response) throws IOException {
        if (callback != null) {
            Type type = getType();
            if (type == Response.class) {
                return (ResultType) response;
            } else if (type == ResponseBody.class) {
                return (ResultType) response.body();
            } else if (type == ResponseSimeple.class) {
                ResponseSimeple simeple = new ResponseSimeple.Builder()
                        .body(response.body())
                        .code(response.code())
                        .headers(response.headers())
                        .message(response.message())
                        .receivedResponseAtMillis(response.receivedResponseAtMillis())
                        .sentRequestAtMillis(response.sentRequestAtMillis())
                        .request(response.request())
                        .build();
                return (ResultType) simeple;
            } else {
                String json = response.body().string();
                if (type == String.class) {
                    return (ResultType) json;
                } else {
                    ResultType res = GsonHelper.getGson().fromJson(json, type);
                    if (res instanceof BaseHttpResponse) {
                        ((BaseHttpResponse) res).json = json;
                        ((BaseHttpResponse) res).httpcode = response.code();
                    }
                    return res;
                }
            }
        }
        return null;
    }

    /**
     * 获取回调里面的泛型
     */
    private Type getType() {
        Type types = callback.getClass().getGenericSuperclass();
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
