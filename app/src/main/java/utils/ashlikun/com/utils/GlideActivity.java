package utils.ashlikun.com.utils;

import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.ashlikun.glideutils.GlideLoad;
import com.ashlikun.glideutils.okhttp.ProgressListener;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/12/29　16:54
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class GlideActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        imageView = (ImageView) findViewById(R.id.image);
        GlideLoad.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515041120482&di=b367fefe8363eced9b3d6028907c5def&imgtype=0&src=http%3A%2F%2Fbbsfiles.vivo.com.cn%2Fvivobbs%2Fattachment%2Fforum%2F201601%2F06%2F113455ibq6ow1e6bb1y14o.jpg")
                .progressListener(new ProgressListener() {
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        Log.e("aaaa", progress + "   " + total + "   " + (Looper.myLooper() == Looper.getMainLooper()));
                    }
                })
                .show(imageView);
    }
}
