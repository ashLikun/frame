package com.hbung.glideutils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by Administrator on 2016/8/8.
 */

public class GlideUtils {

    private static OnNeedListener listener;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/7 10:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：一定要在Application里面调用
     */

    public static void init(OnNeedListener listener) {
        GlideUtils.listener = listener;
    }


    public interface OnNeedListener {
        public Application getApplication();

        public boolean isDebug();

        public String getBaseUrl();
    }

    public static Application getApp() {
        if (listener == null) {
            throw new RuntimeException("请在Application调用Utils的init方法");
        } else {
            return listener.getApplication();
        }
    }

    public static boolean isDebug() {
        if (listener == null) {
            throw new RuntimeException("请在Application调用Utils的init方法");
        } else {
            return listener.isDebug();
        }
    }

    public static String getBaseUrl() {
        if (listener == null) {
            throw new RuntimeException("请在Application调用Utils的init方法");
        } else {
            return listener.getBaseUrl();
        }
    }

    public static void show(ImageView view, String path, GlideOptions picassoOptions) {
        DrawableTypeRequest drawableTypeRequest = getRequestCreator(view.getContext(), path);
        show(drawableTypeRequest, view, picassoOptions);
    }

    public static void show(Fragment fragment, ImageView view, String path, GlideOptions picassoOptions) {
        DrawableTypeRequest drawableTypeRequest = getRequestCreator(fragment, path);
        show(drawableTypeRequest, view, picassoOptions);
    }

    private static void show(DrawableTypeRequest drawableTypeRequest, ImageView view, GlideOptions picassoOptions) {
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
            if (a instanceof FragmentActivity) {
                return Glide.with((FragmentActivity) a).load(getHttpFileUrl(path));
            } else {
                return Glide.with(a).load(getHttpFileUrl(path));
            }
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
                    new GlideOptions.Builder().addTransformation(new CropCircleTransformation(getApp())).bulider());

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

        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> e) throws Exception {
                File file = Glide.with(getApp()).load(getHttpFileUrl(url))
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                e.onNext(file);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
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
            if (url.startsWith("http://") || url.startsWith("https://")) {
                res = url;
            } else if (url.startsWith("/storage") || url.startsWith("storage") || url.startsWith("/data") || url.startsWith("data")) {
                res = url;
            } else if (url.startsWith("/")) {
                res = getBaseUrl() + url;
            } else {
                res = getBaseUrl() + "/" + url;
            }
        }
        return res;
    }

}
