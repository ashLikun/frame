package com.hbung.http.httpquest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/24　16:20
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class RequestBodyUtils {


    //=========================================================================转换方法================================================================


    /**
     * String文本 转成RequestBody
     *
     * @param value
     * @return
     */

    public static RequestBody getStringRequestBody(String value) {
        if (value == null) {
            return null;
        }
        return RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), value);
    }


    /**
     * 文件 转成RequestBody
     *
     * @param file
     * @return
     */
    public static RequestBody getFileRequestBody(File file) {
        if (!file.exists()) {
            return null;
        }
        return RequestBody.create(MultipartBody.FORM, file);
    }

    /**
     * 文件路径 转成RequestBody
     *
     * @param filePath
     * @return
     */
    public static RequestBody getFileRequestBody(String filePath) {
        File file = new File(filePath);
        return getFileRequestBody(file);
    }


    /**
     * FileParam集合 转成Map
     *
     * @param params
     * @return
     */
    public static Map<String, RequestBody> getFileParamsRequestBody(List<FileParam>... params) {
        Map<String, RequestBody> map = new HashMap<>();
        for (List<FileParam> lp : params)
            for (FileParam p : lp) {
                if (p.exists()) {
                    map.put(p.getMapKey(), p.getFileRequestBody());
                }
            }
        return map;
    }

    public static Map<String, RequestBody> getFileParamsRequestBody(FileParam... params) {

        List<FileParam> a = new ArrayList();
        for (FileParam p : params) {
            if (p.exists()) {
                a.add(p);
            }
        }
        return getFileParamsRequestBody(a);
    }


    /**
     * String集合 转成Map
     *
     * @param paths
     * @return
     */
    public static Map<String, RequestBody> getFileStringsRequestBody(List<String> paths) {
        return getFileParamsRequestBody(FileParam.getStringToFileParam(paths));
    }

    /**
     * File集合 转成Map
     *
     * @param paths
     * @return
     */
    public static Map<String, RequestBody> getFilesRequestBody(List<File>... paths) {
        return getFileParamsRequestBody(FileParam.getFileToFileParam(paths));
    }

    /**
     * 单个String 转成Map
     *
     * @param paths
     * @return
     */
    public static Map<String, RequestBody> getFileStringRequestBody(String paths) {
        List<String> p = new ArrayList<>();
        p.add(paths);
        return getFileParamsRequestBody(FileParam.getStringToFileParam(p));
    }

}
