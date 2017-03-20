package com.hbung.utils.other;

import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 10:46
 * <p>
 * 方法功能：textVIew 倒计时工具
 */

public class CountdownUtils {
    /**
     * 验证码按钮倒计时 总时长和间隔时长，默认为60秒
     */
    private static long totalTime = 180000, intervalTime = 1000;


    /**
     * 倒计时
     *
     * @param hint
     */
    public static void recordTime(final TextView hint, final String msg) {

        /**
         * 记录当下剩余时间
         */
        CountDownTimer countDownTimer = new CountDownTimer(totalTime, intervalTime) {
            @Override
            public void onFinish() {// 计时完毕时触发
                hint.setText(msg);
                hint.setEnabled(true);
            }

            @Override
            public void onTick(long currentTime) {// 计时过程显示
                hint.setEnabled(false);
                hint.setText(currentTime / 1000 + "秒");
            }
        };

        countDownTimer.start();

    }

    public static void recordTime(final TextView hint) {
        recordTime(hint, "重新发送");
    }

}
