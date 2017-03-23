package com.hbung.http.request;

import android.net.Uri;

import com.hbung.http.Callback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者　　: 李坤
 * 创建时间:2016/10/14　15:03
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍： 请求参数封装
 * 注意：一定要调用请求方法指定请求的方法，默认时get
 */

public class RequestParam {
    protected String url;//请求地址
    private String path;//请求路径
    private String method;//请求方法
    protected Map<String, String> headers;//请求头

    protected Map<String, String> params;//普通键值对参数  get
    private String postContent;//请求内容，如果设置这个参数  其他的参数将不会提交  post
    private List<FileInput> files;//上传文件

    public String getPath() {
        return path;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void post() {
        method = "POST";
    }

    public void get() {
        method = "GET";
    }

    public void url(String url) {
        this.url = url;
    }

    public boolean isHavaHeader() {
        return headers != null && !headers.isEmpty();
    }

    public boolean isHavaparams() {
        return params != null && !params.isEmpty();
    }

    public boolean isHavafiles() {
        return files != null && !files.isEmpty();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addAction(String valuse) {
        addParam("action", valuse);
    }

    public RequestParam(String action) {
        addAction(action);
    }


    public RequestParam() {
    }


    public void addParam(String key, String valuse) {
        if (!isEmpty(key) && !isEmpty(valuse)) {
            if (params == null) params = new HashMap<>();
            params.put(key, valuse);
        }
    }

    public void addHeader(String key, String valuse) {
        if (!isEmpty(key) && !isEmpty(valuse)) {
            if (headers == null) headers = new HashMap<>();
            headers.put(key, valuse);
        }
    }

    public void addParam(String key, int valuse) {
        addParam(key, String.valueOf(valuse));
    }

    public void addParam(String key, double valuse) {
        addParam(key, String.valueOf(valuse));
    }

    public void addParam(String key, File file) {
        FileInput param = new FileInput(key, file);
        if (param.exists()) {
            if (files == null) files = new ArrayList<>();
            files.add(param);
        }
    }

    public void addParamFile(String key, String filePath) {
        if (filePath == null) return;
        addParam(key, new File(filePath));
    }

    public void addParam(String key, List<File> files) {
        if (files == null || files.isEmpty()) return;
        for (File f : files) {
            addParam(key, f);
        }
    }

    public void addParamFile(String key, List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) return;
        for (String f : filePaths) {
            addParamFile(key, f);
        }
    }

    public void setContent(String content) {
        postContent = content;
    }

    public Request generateRequest(Callback callback) {
        Request.Builder builder = new Request.Builder();
        if (isEmpty(method))
            method = "GET";
        RequestBody requestBody = buildRequestBody(callback);
        Headers.Builder header = new Headers.Builder();
        if (isHavaHeader()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                header.add(entry.getKey(), entry.getValue());
            }
        }
        return builder
                .url(url)
                .headers(header.build())
                .method(method, requestBody)
                .build();
    }

    public RequestBody buildRequestBody(Callback callback) {
        RequestBody body = null;
        if (method.equals("GET")) {
            //get请求把参数放在url里面, 没有请求实体
            url = appendQueryParams(url, params);
            body = null;
        } else if (!isEmpty(postContent)) {//只提交content
            body = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), postContent);
        } else {

            if (isHavafiles()) {//存在文件用MultipartBody
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                addParams(builder);
                addFlieParams(builder);
                body = builder.build();
            } else {//不存在文件用 FormBody
                FormBody.Builder builder = new FormBody.Builder();
                addParams(builder);
                body = builder.build();
            }
        }
        //是否添加进度回调
        if (body != null && callback != null && callback instanceof ProgressCallBack) {
            body = new ProgressRequestBody(body, (ProgressCallBack) callback);
        }
        return body;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/21 10:00
     * <p>
     * 方法功能：get请求构建url
     */

    private String appendQueryParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build().toString();
    }

    /**
     * 是否正常的字符串
     *
     * @return
     */
    public boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/21 9:55
     * <p>
     * 方法功能：构建请求body    多表单数据  键值对
     */
    private void addParams(MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());//Content-Disposition;form-data; name="aaa"
            }
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/21 9:55
     * <p>
     * 方法功能：构建请求body    表单数据  键值对
     */
    private void addParams(FormBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addEncoded(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/21 9:55
     * <p>
     * 方法功能：构建请求body    多表单  文件数据
     */
    private void addFlieParams(MultipartBody.Builder builder) {//表单数据
        if (files != null && !files.isEmpty()) {
            for (FileInput fileInput : files) {
                builder.addFormDataPart(fileInput.key, fileInput.filename
                        , RequestBody.create(MediaType.parse(getMimeType(fileInput.file.getAbsolutePath())), fileInput.file));
            }
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/21 9:56
     * <p>
     * 方法功能：获取文件的mime类型  Content-type
     */
    private String getMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        try {
            return fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "application/octet-stream";
    }
}
