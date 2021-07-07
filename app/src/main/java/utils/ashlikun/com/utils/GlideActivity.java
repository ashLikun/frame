package utils.ashlikun.com.utils;

import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;


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
    }
}
