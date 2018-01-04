package com.ashlikun.glideutils.okhttp;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/5/23 10:29
 * <p>
 * 方法功能：进度回调
 */
public interface ProgressListener {

    /**
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     */
    void onProgress(long progress, long total, boolean done);

}
