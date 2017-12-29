package utils.ashlikun.com.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ashlikun.glideutils.GlideUtils;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;

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
        GlideUtils.show(imageView, R.mipmap.image);
    }
}
