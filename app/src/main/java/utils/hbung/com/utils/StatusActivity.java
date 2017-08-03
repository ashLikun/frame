package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hbung.utils.Utils;
import com.hbung.utils.ui.StatusBarCompat;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/3 0003　22:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class StatusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(getApplication(), false);
        new StatusBarCompat(this).setTransparentBar();
        setContentView(R.layout.activity_main);

    }
}
