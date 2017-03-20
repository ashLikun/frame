/*
 * Copyright 2014-2016 wjokhttp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hbung.http.download;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * 下载管理器
 */
public class DownloadManager {
    public static String defaultFilePath;

    private OkHttpClient mClient;

    // 将执行结果保存
    private Map<String, DownloadTask> mCurrentTaskList;

    static DownloadManager manager;

    /**
     * 方法加锁，防止多线程操作时出现多个实例
     */
    public static synchronized void init(OkHttpClient client, String defaultFilePath) {
        if (manager == null) {
            DownloadManager.defaultFilePath = defaultFilePath;
            manager = new DownloadManager(client);
        }
    }

    /**
     * 获得当前对象实例
     *
     * @return 当前实例对象
     */
    public final static DownloadManager getInstance() {
        if (manager == null) new Exception("请调用init方法");
        return manager;
    }


    private DownloadManager(OkHttpClient client) {
        initOkhttpClient(client);
        mCurrentTaskList = new HashMap<>();

    }

    /**
     * 初始化okhttp
     */
    private void initOkhttpClient(OkHttpClient client) {
        mClient = client;
    }

    /**
     * 添加下载任务
     *
     * @param downloadTask
     */
    public void addDownloadTask(DownloadTask downloadTask) {

        if (downloadTask != null && !isDownloading(downloadTask)) {
            DownloadTask oldTask = mCurrentTaskList.get(downloadTask.getId());
            if (oldTask != null) {
                if (isDownloading(oldTask)) return;
                else {
                    oldTask.onCompleted();
                    mCurrentTaskList.remove(oldTask);
                }
            }

            downloadTask.setClient(mClient);
            downloadTask.setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_INIT);
            // 保存下载task列表
            mCurrentTaskList.put(downloadTask.getId(), downloadTask);
            Subscription subscription = Observable.just(1).subscribeOn(Schedulers.io())//指定 subscribe() 发生在 IO 线程
                    .observeOn(Schedulers.io()).//指定回调在io线程
                    subscribe(downloadTask);
            //保存Subscription 方便取消订阅
            downloadTask.setSubscription(subscription);
        }
    }

    private boolean isDownloading(DownloadTask task) {
        if (task != null) {
            if (task.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING) {
                return true;
            }
        }
        return false;
    }

    /**
     * 暂停下载任务
     *
     * @param id 任务id
     */
    public void pause(String id) {
        DownloadTask task = getDownloadTask(id);
        if (task != null) {
            task.setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_PAUSE);
        }
    }

    /**
     * 重新开始已经暂停的下载任务
     *
     * @param id 任务id
     */
    public void resume(String id) {
        DownloadTask task = getDownloadTask(id);
        if (task != null) {
            addDownloadTask(task);
        }
    }

    /**
     * 取消下载任务(同时会删除已经下载的文件，和清空数据库缓存)
     *
     * @param id 任务id
     */
    public void cancel(String id) {

        DownloadTask task = getDownloadTask(id);
        if (task != null) {
            task.cancel();
            task.setDownloadStatus(DownloadStatus.DOWNLOAD_STATUS_CANCEL);
            Subscription subscription = task.getSubscription();
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }


    /**
     * 实时更新manager中的task信息
     *
     * @param task
     */
    public void updateDownloadTask(DownloadTask task) {
        if (task != null) {
            DownloadTask currTask = getDownloadTask(task.getId());
            if (currTask != null) {
                mCurrentTaskList.put(task.getId(), task);
            }
        }
    }

    /**
     * 获得指定的task
     *
     * @param id task id
     * @return
     */
    public DownloadTask getDownloadTask(String id) {
        DownloadTask currTask = mCurrentTaskList.get(id);
        if (currTask == null) {
            // 从数据库中取出为完成的task
            DownloadEntity entity = DownloadEntity.queryById(id);
            if (entity != null) {
                if (entity.getDownloadStatus() != DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
                    currTask = parseEntity2Task(entity);
                    // 放入task list中
                    mCurrentTaskList.put(id, currTask);
                }
            }
        }
        return currTask;
    }

    /**
     * 获得所有的task
     *
     * @return
     */
    public Map<String, DownloadTask> getAllDownloadTasks() {
        if (mCurrentTaskList != null && mCurrentTaskList.size() <= 0) {
            List<DownloadEntity> entitys = DownloadEntity.queryAll();
            for (DownloadEntity entity : entitys) {
                DownloadTask currTask = parseEntity2Task(entity);
                mCurrentTaskList.put(entity.getDownloadId(), currTask);
            }
        }

        return mCurrentTaskList;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/2 10:35
     * <p>
     * 方法功能：把从数据库取出来的实体  放入任务栈
     */

    private DownloadTask parseEntity2Task(DownloadEntity entity) {
        if (entity != null) {
            DownloadTask.Builder builder = new DownloadTask.Builder()//
                    .setDownloadStatus(entity.getDownloadStatus())//
                    .setFileName(entity.getFileName())//
                    .setSaveDirPath(entity.getSaveDirPath())//
                    .setUrl(entity.getUrl())//
                    .setId(entity.getDownloadId());//
            DownloadTask currTask = new DownloadTask.Builder()
                    .setDownloadStatus(entity.getDownloadStatus())//
                    .setFileName(entity.getFileName())//
                    .setSaveDirPath(entity.getSaveDirPath())//
                    .setUrl(entity.getUrl())//
                    .setId(entity.getDownloadId()).build();//
            currTask.setCompletedSize(entity.getCompletedSize());//
            currTask.setTotalSize(entity.getTotalSize());
            return currTask;
        }
        return new DownloadTask.Builder().build();
    }
}
