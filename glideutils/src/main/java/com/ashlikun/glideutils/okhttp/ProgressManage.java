package com.ashlikun.glideutils.okhttp;

import com.bumptech.glide.load.model.GlideUrl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/1/3　17:55
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class ProgressManage {
    public static final long DEFAULT_LENGTH = 100;

    private static class ProgressMode {
        ProgressListener progressListener;
        long totalBytesRead;
        long contentLength;

        public ProgressMode(ProgressListener progressListener, long totalBytesRead, long contentLength) {
            this.progressListener = progressListener;
            this.totalBytesRead = totalBytesRead;
            this.contentLength = contentLength;
        }
    }

    private static Map<Object, ProgressMode> listenerMap;

    public static long getTotalBytesRead(Object mode) {
        if (listenerMap == null) {
            return DEFAULT_LENGTH;
        }
        ProgressMode progressMode = listenerMap.get(mode);
        if (progressMode != null) {
            return progressMode.totalBytesRead;
        }
        return DEFAULT_LENGTH;
    }

    public static long getContentLength(Object mode) {
        if (listenerMap == null) {
            return DEFAULT_LENGTH;
        }
        ProgressMode progressMode = listenerMap.get(mode);
        if (progressMode != null) {
            return progressMode.contentLength;
        }
        return DEFAULT_LENGTH;
    }

    public static void add(Object mode, ProgressListener listener) {
        if (mode == null || listener == null) {
            return;
        }
        if (listenerMap == null) {
            listenerMap = new ConcurrentHashMap<>();
        }
        listenerMap.put(mode, new ProgressMode(listener, DEFAULT_LENGTH, DEFAULT_LENGTH));
    }

    public static void remove(Object mode) {
        if (mode == null) {
            return;
        }
        if (listenerMap == null) {
            listenerMap = new ConcurrentHashMap<>();
        }
        listenerMap.remove(mode);
    }

    public static boolean isExist(GlideUrl url) {
        if (listenerMap == null) {
            return false;
        }
        return listenerMap.containsKey(url.getCacheKey());
    }

    public static void onProgress(GlideUrl url, long progress, long total, boolean done) {
        ProgressMode listener = listenerMap.get(url.getCacheKey());
        if (listener != null && listener.progressListener != null) {
            listener.totalBytesRead = progress;
            listener.contentLength = total;
            listener.progressListener.onProgress(progress, total, done);
        }
    }
}
