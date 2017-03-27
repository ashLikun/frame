package com.hbung.glideutils;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/8/8.
 */

public class GlideUtils {
    public static Application myApp;
    public static String BASE_URL;

    public static void init(Application application, String BASE_URL) {
        GlideUtils.myApp = application;
        GlideUtils.BASE_URL = BASE_URL;
//        OkHttpClient okHttpClient = (OkHttpClient) HttpManager.getInstance().getRetrofit().callFactory();
//        OkHttpDownLoader okHttpDownloader = new OkHttpDownLoader(okHttpClient);
        Glide.get(application).setMemoryCategory(MemoryCategory.LOW);

    }

    public static void show(ImageView view, String path, GlideOptions picassoOptions) {
        DrawableTypeRequest drawableTypeRequest = getRequestCreator(view.getContext(), path);
        if (picassoOptions.getError() > 0) {
            drawableTypeRequest.error(picassoOptions.getError());
        }
        if (picassoOptions.getPlaceholder() > 0) {
            drawableTypeRequest.placeholder(picassoOptions.getPlaceholder());
        }
        if (picassoOptions.getTransformations() != null && picassoOptions.getTransformations().length > 0) {
            drawableTypeRequest.bitmapTransform(picassoOptions.getTransformations());
        }
        if (picassoOptions.getWidth() > 0 && picassoOptions.getHeight() > 0) {
            drawableTypeRequest.override(picassoOptions.getWidth(), picassoOptions.getHeight());
        }
        if (picassoOptions.getRequestListener() != null) {
            drawableTypeRequest.listener(picassoOptions.getRequestListener());
        }
        drawableTypeRequest.sizeMultiplier(0.8f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    public static DrawableTypeRequest<String> getRequestCreator(Context context, String path) {
        Activity a = getActivity(context);
        if (a != null) {
            return Glide.with(a).load(getHttpFileUrl(path));
        } else {
            return Glide.with(context).load(getHttpFileUrl(path));
        }
    }

    public static DrawableTypeRequest<String> getRequestCreator(Fragment fragment, String path) {
        return Glide.with(fragment).load(getHttpFileUrl(path));
    }


    /**
     * 给ImageView 设置网络图片（圆形）
     *
     * @param imageView
     * @param path
     */
    public static void showCircle(View imageView, String path) {
        try {
            GlideUtils.show((ImageView) imageView,
                    path,
                    new GlideOptions.Builder().addTransformation(new CropCircleTransformation(myApp)).bulider());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 给ImageView 设置网络图片
     *
     * @param imageView
     * @param path
     */
    public static void show(View imageView, String path) {
        try {
            GlideUtils.show((ImageView) imageView,
                    path,
                    new GlideOptions.Builder().bulider());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 下载
     *
     * @param url
     * @param downloadCallbacl
     */
    public static void downloadOnly(final String url, final OnDownloadCallbacl downloadCallbacl) {

        Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                File file = null;
                try {
                    file = Glide.with(myApp).load(getHttpFileUrl(url))
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(file);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        downloadCallbacl.onCall(file);
                    }
                });
    }


    public static interface OnDownloadCallbacl {
        void onCall(File file);
    }

    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /*
     * 对网络资源文件判断路径 如果是已http开头的就返回这个值 否则在前面加上ORG
     */
    public static String getHttpFileUrl(String url) {
        String res = "";
        if (url != null) {
            if (url.startsWith("http://")) {
                res = url;
            } else if (url.startsWith("/storage") || url.startsWith("storage") || url.startsWith("/data") || url.startsWith("data")) {
                res = url;
            } else if (url.startsWith("/")) {
                res = BASE_URL + url;
            } else {
                res = BASE_URL + "/" + url;
            }
        }
        return res;
    }

}
