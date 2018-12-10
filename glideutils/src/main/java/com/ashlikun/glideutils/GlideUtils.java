package com.ashlikun.glideutils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;
import java.lang.reflect.Field;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/12/29 16:59
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Glide的封装
 * Object的参数可以是资源id，网络文件，本地文件，url
 */

public class GlideUtils {

    private static boolean DEBUG;
    private static String BASE_URL;

    static DiskLruCacheFactory diskLruCacheFactory = null;


    public static void setDiskLruCacheFactory(DiskLruCacheFactory diskLruCacheFactory) {
        GlideUtils.diskLruCacheFactory = diskLruCacheFactory;
    }

    /**
     * 是否调试
     */
    public static void setDEBUG(boolean DEBUG) {
        GlideUtils.DEBUG = DEBUG;
    }

    /**
     * 设置域名，当网络文件不是以Http打头的时候会加上
     *
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        GlideUtils.BASE_URL = baseUrl;
    }


    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * 给ImageView 设置网络图片（圆形）
     */
    public static ViewTarget<ImageView, Drawable> showCircle(ImageView imageView, Object path) {
        return show(imageView, path, getCircleOptions());
    }

    public static ViewTarget<ImageView, Drawable> show(ImageView imageView, Object path) {
        return show(imageView, path, null);
    }

    public static ViewTarget<ImageView, Drawable> show(ImageView view, Object path, RequestOptions requestOptions) {
        return GlideLoad.with(view)
                .load(path)
                .options(requestOptions)
                .show(view);
    }

    public static Target<Drawable> show(Context context, Target view, Object path, RequestOptions requestOptions) {
        return GlideLoad.with(context)
                .load(path)
                .options(requestOptions)
                .show(view);
    }

    public static ViewTarget<ImageView, Drawable> show(Fragment fragment, ImageView view, Object path, RequestOptions requestOptions) {
        return GlideLoad.with(fragment)
                .load(path)
                .options(requestOptions)
                .show(view);
    }

    public static Target<Drawable> show(Fragment fragment, Target view, Object path, RequestOptions requestOptions) {
        return GlideLoad.with(fragment)
                .load(path)
                .options(requestOptions)
                .show(view);
    }


    /**
     * 下载
     *
     * @param url
     * @param downloadCallbacl
     */
    public static void downloadBitmap(final Context context, final String url, final OnDownloadCallback downloadCallbacl) {
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> e) throws Exception {
                File file = Glide.with(context).download(getHttpFileUrl(url))
                        .submit().get();
                e.onNext(file);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        downloadCallbacl.onCall(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        downloadCallbacl.onCall(null);
                    }
                });

    }

    /**
     * 对网络资源文件判断路径 如果是已http开头的就返回这个值 否则在前面加上ORG
     */
    public static Object getHttpFileUrl(Object url) {
        if (url instanceof String) {
            String res = "";
            if (url != null) {
                if (((String) url).startsWith("http://") || ((String) url).startsWith("https://")) {
                    res = (String) url;
                } else if (((String) url).startsWith("/storage") || ((String) url).startsWith("storage") || ((String) url).startsWith("/data") || ((String) url).startsWith("data")) {
                    res = (String) url;
                } else if (((String) url).startsWith("/")) {
                    res = getBaseUrl() + url;
                } else {
                    res = getBaseUrl() + "/" + url;
                }
            }
            return res;
        }
        return url;
    }

    /**
     * 获取中间裁剪的圆角图
     *
     * @return
     */
    public static RequestOptions getRoundedOptions(int radius) {
        return new RequestOptions().transforms(new CenterCrop(),
                new RoundedCornersTransformation(radius, 0));
    }

    /**
     * 获取中间裁剪的圆形
     *
     * @return
     */
    public static RequestOptions getCircleOptions() {
        return new RequestOptions().transforms(new CenterCrop(),
                new CircleCrop());
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static double getCacheSize(Context context) {
        try {
            return getFolderSize(getCacheDir(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {
        if (diskLruCacheFactory == null) {
            diskLruCacheFactory = new InternalCacheDiskCacheFactory(context);
        }
        //反射获取私有属性
        try {
            Field field = DiskLruCacheFactory.class.getDeclaredField("cacheDirectoryGetter");
            field.setAccessible(true);
            DiskLruCacheFactory.CacheDirectoryGetter cacheDirectoryGetter = (DiskLruCacheFactory.CacheDirectoryGetter) field.get(diskLruCacheFactory);
            return cacheDirectoryGetter.getCacheDirectory();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这个图片是否有缓存
     * 只能在子线程
     *
     * @param url
     * @return
     */
    public static boolean isCache(Context context, String url) {
        return getCache(context, url) != null;
    }

    /**
     * 获取缓存文件
     * 只能在子线程
     * onlyRetrieveFromCache 是否只是从缓存中获取
     *
     * @param context
     * @param url
     * @return
     */
    public static File getCache(Context context, String url) {
        try {
            if (diskLruCacheFactory == null) {
                diskLruCacheFactory = new InternalCacheDiskCacheFactory(context);
            }
            DiskCache wrapper = diskLruCacheFactory.build();
            return wrapper.get(new GlideUrl(url));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

}
