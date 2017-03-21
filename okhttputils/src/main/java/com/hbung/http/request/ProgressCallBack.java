package com.hbung.http.request;

/**
 * Created by Administrator on 2016/8/3.
 */

public interface ProgressCallBack {


    /**
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     * @param isUpdate 是否是上传
     */
    void onLoading(long progress, long total, boolean done, boolean isUpdate);

    /**
     * @return
     */
    long getRate();

    /**
     * @param rate
     */
    void setRate(long rate);


}
