package com.hbung.http.mode;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/24　16:02
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基本请求方式的服务器接口
 */

public interface BaseServiceApi {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                 要传具体的url路径  如ashx,php,html
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> post(
            @Path(value = "path", encoded = true) String path,
            @FieldMap Map<String, String> maps
    );


    @GET("{path}")
    Observable<ResponseBody> get(
            @Path(value = "path", encoded = true) String path,
            @QueryMap Map<String, String> maps);


    @Multipart
    @POST("{path}")
    Observable<ResponseBody> uploadFiles(
            @Path(value = "path", encoded = true) String path,
            @PartMap() Map<String, RequestBody> maps);

    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> post(
            @HeaderMap Map<String, String> headers,
            @Path(value = "path", encoded = true) String path,
            @FieldMap Map<String, String> maps
    );


    @GET("{path}")
    Observable<ResponseBody> get(
            @HeaderMap Map<String, String> headers,
            @Path(value = "path", encoded = true) String path,
            @QueryMap Map<String, String> maps);


    @Multipart
    @POST("{path}")
    Observable<ResponseBody> uploadFiles(
            @HeaderMap Map<String, String> headers,
            @Path(value = "path", encoded = true) String path,
            @PartMap() Map<String, RequestBody> maps);


    @Streaming
    @GET
    Call downloadFile(@Url String fileUrl, @PartMap() Map<String, RequestBody> maps);

}
