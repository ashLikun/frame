package com.ashlikun.glideutils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/12/29 16:59
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Glide的封装
 * Object的参数可以是资源id，网络文件，本地文件，url
 */

public class GlideUtils {

    private static RequestOptions REQUEST_OPTIONS = new RequestOptions();
    private static final DrawableTransitionOptions TRANSITION_OPTIONS = new DrawableTransitionOptions().crossFade();
    private static Application application;
    private static boolean debug;
    private static String baseUrl;

    {
        REQUEST_OPTIONS.error(R.drawable.material_default_image_1_1);
        REQUEST_OPTIONS.placeholder(R.color.glide_placeholder_color);
        REQUEST_OPTIONS.sizeMultiplier(0.8f);
        REQUEST_OPTIONS.diskCacheStrategy(DiskCacheStrategy.ALL);
        REQUEST_OPTIONS.autoClone();
    }

    /**
     * 初始化请求参数 默认为上面的
     * 要全局处理，一次调用
     */
    public static void initRequestOptions(RequestOptions requestOptions) {
        REQUEST_OPTIONS = REQUEST_OPTIONS.apply(requestOptions);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/7 10:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：一定要在Application里面调用
     */

    public static void init(Application application) {
        GlideUtils.application = application;
    }

    /**
     * 是否调试
     */
    public static void setDebug(boolean debug) {
        GlideUtils.debug = debug;
    }

    /**
     * 设置域名，当网络文件不是以Http打头的时候会加上
     *
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        GlideUtils.baseUrl = baseUrl;
    }

    public static Application getApp() {
        return application;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 给ImageView 设置网络图片（圆形）
     */
    public static void showCircle(View imageView, Object path) {
        try {
            GlideUtils.show((ImageView) imageView,
                    path,
                    new RequestOptions().circleCrop());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ViewTarget<ImageView, Drawable> show(ImageView imageView, Object path) {
        try {
            return show(imageView, path, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ViewTarget<ImageView, Drawable> show(ImageView view, Object path, RequestOptions requestOptions) {
        scaleType(view, requestOptions);
        return getRequestBuilder(getRequest(view.getContext(), path), requestOptions).into(view);
    }

    public static Target<Drawable> show(Context context, Target view, Object path, RequestOptions requestOptions) {
        return getRequestBuilder(getRequest(context, path), requestOptions).into(view);
    }

    public static ViewTarget<ImageView, Drawable> show(Fragment fragment, ImageView view, Object path, RequestOptions requestOptions) {
        scaleType(view, requestOptions);
        return getRequestBuilder(getRequest(fragment, path), requestOptions).into(view);
    }

    public static Target<Drawable> show(Fragment fragment, Target view, Object path, RequestOptions requestOptions) {
        return getRequestBuilder(getRequest(fragment, path), requestOptions).into(view);
    }


    public static RequestBuilder<Drawable> getRequestBuilder(Fragment fragment, Object path, RequestOptions requestOptions) {
        return getRequestBuilder(getRequest(fragment, path), requestOptions);
    }

    public static RequestBuilder<Drawable> getRequestBuilder(RequestBuilder<Drawable> drawableTypeRequest, RequestOptions requestOptions) {
        if (requestOptions != null) {
            return drawableTypeRequest.apply(REQUEST_OPTIONS.apply(requestOptions)).transition(TRANSITION_OPTIONS);
        } else {
            return drawableTypeRequest.apply(REQUEST_OPTIONS).transition(TRANSITION_OPTIONS);
        }

    }

    public static RequestBuilder<Drawable> getRequest(Context context, Object path) {
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

    public static RequestBuilder<Drawable> getRequest(Fragment fragment, Object path) {
        return Glide.with(fragment).load(getHttpFileUrl(path));
    }

    private static void scaleType(ImageView imageView, RequestOptions requestOptions) {
        if (imageView != null && imageView.getScaleType() == null) {
            requestOptions.centerCrop();
        }

    }

    /**
     * 下载
     *
     * @param url
     * @param downloadCallbacl
     */
    public static void downloadBitmap(final String url, final OnDownloadCallbacl downloadCallbacl) {
        Observable.create((ObservableEmitter<File> e) -> {
            File file = Glide.with(getApp()).download(getHttpFileUrl(url))
                    .submit().get();
            e.onNext(file);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((File file) -> {
                    downloadCallbacl.onCall(file);
                });
    }


    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
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

}
