package utils.hbung.com.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import okhttp3.FormBody;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/16　9:48
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class UpDataIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpDataIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
