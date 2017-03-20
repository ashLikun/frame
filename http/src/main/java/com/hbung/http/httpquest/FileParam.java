package com.hbung.http.httpquest;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/8/19.
 */

public class FileParam {
    String key;
    String path;
    String fileName;

    private File file = null;


    public static List<FileParam> addFileParam(List<FileParam> params, FileParam fileParam) {
        if (fileParam.exists()) {
            params.add(fileParam);
        }

        return params;
    }

    public static List<FileParam> getStringToFileParam(List<String> paths) {
        return getStringToFileParam(null, paths);
    }

    public static List<FileParam> getStringToFileParam(String key, List<String>... paths) {
        List<FileParam> params1 = new ArrayList<>();
        for (List<String> items : paths)
            for (String p : items) {
                if (TextUtils.isEmpty(p)) {
                    FileParam pf = new FileParam(key, p);
                    if (pf.exists()) {
                        params1.add(pf);
                    }
                }
            }
        return params1;
    }

    public static List<FileParam> getFileToFileParam(List<File>... paths) {

        return getFileToFileParam(null, paths);
    }

    public static List<FileParam> getFileToFileParam(String key, List<File>... paths) {
        List<FileParam> params1 = new ArrayList<>();
        for (List<File> pp : paths)
            for (File p : pp) {
                if (p.exists()) {
                    params1.add(new FileParam(key, p));
                }
            }
        return params1;
    }


    public FileParam(String path) {
        this.path = path;
    }

    public FileParam(File file) {
        this.file = file;
        if (exists()) {
            this.path = file.getPath();
        }
    }

    public FileParam(String key, File file) {
        this.key = key;
        this.file = file;
        if (exists()) {
            this.path = file.getPath();
        }
    }

    public FileParam(String key, String path) {
        this.key = key;
        this.path = path;
        file = new File(path);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean exists() {

        try {
            return getFile().exists();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public File getFile() throws Exception {

        return file == null ? (file = new File(path)) : file;
    }

    public RequestBody getFileRequestBody() {
        return RequestBodyUtils.getFileRequestBody(path);
    }

    public String getMapKey() {
        StringBuilder sb = new StringBuilder();
        if (key == null || key.length() == 0) {
            sb.append("file\";");
        } else {
            sb.append(key);
            sb.append("\"; ");
        }
        sb.append("filename=\"");
        if (fileName == null || fileName.length() == 0) {
            if (exists()) {
                fileName = file.getName();
            } else {
                fileName = "noFile";
            }
        }
        sb.append(fileName);
        return sb.toString();
    }
}
