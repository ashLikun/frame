package com.hbung.http.httpquest;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 作者　　: 李坤
 * 创建时间:2016/10/14　15:03
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍： 请求参数
 */

public class RequestParam {
    Map<String, RequestBody> partMap = new HashMap<>();
    Map<String, String> mapString = new HashMap<>();
    Map<String, String> headers;
    private String path;//请求路径

    public String getPath() {
        return path;
    }

    public boolean isHavaHeader() {
        return headers != null && !headers.isEmpty();
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
            mapString.put(key, valuse);
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
        FileParam param = new FileParam(key, file);
        if (param.exists())
            partMap.put(param.getMapKey(), param.getFileRequestBody());
    }

    public void addParamFile(String key, String filePath) {
        if (filePath == null) return;
        addParam(key, new File(filePath));
    }

    public void addParam(String key, List<File> files) {
        if (files == null) return;
        List<FileParam> params = FileParam.getFileToFileParam(key, files);
        for (FileParam param : params) {
            if (param.exists())
                partMap.put(param.getMapKey(), param.getFileRequestBody());
        }
    }

    public void addParamFile(String key, List<String> filePaths) {
        if (filePaths == null) return;
        List<FileParam> params = FileParam.getStringToFileParam(key, filePaths);
        for (FileParam param : params) {
            if (param.exists())
                partMap.put(param.getMapKey(), param.getFileRequestBody());
        }
    }

    public Map<String, RequestBody> getPartMap() {
        for (Map.Entry<String, String> entry : mapString.entrySet()) {
            partMap.put(entry.getKey(), RequestBodyUtils.getStringRequestBody(entry.getValue()));
        }
        return partMap;
    }

    public Map<String, String> getStringMap() {
        return mapString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 是否正常的字符串
     *
     * @return
     */
    public boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }


}
