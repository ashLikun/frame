package com.hbung.utils.ui;

import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.hbung.utils.other.DimensUtils;

import java.util.ArrayList;
import java.util.List;
import static com.hbung.utils.ui.UiUtils.setViewMeasure;

/**
 * 作者　　: 李坤
 * 创建时间:2016/10/11　16:42
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class TimePickerUtils {


    /**
     * DatePicker 或者 TimePicker 调整大小
     *
     * @param tp
     */
    public static void setPikcerSize(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (int i = 0; i < npList.size(); i++) {
            setNumberPickerSize(npList.get(i), tp instanceof DatePicker ? i == 0 : false);
            setNumberPickerInput(npList.get(i));
        }
    }

    /**
     * 调整numberpicker大小
     */
    private static void setNumberPickerSize(NumberPicker np, boolean isOne) {
        setViewMeasure(np);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Math.round(np.getMeasuredWidth() / (isOne ? 1.2f : 2.2f)), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(DimensUtils.dip2px(np.getContext(), 4), 0, DimensUtils.dip2px(np.getContext(), 4), 0);
        np.setLayoutParams(params);


    }

    /**
     * 调整numberpicker显示为数字
     */
    private static void setNumberPickerInput(ViewGroup np) {
        for (int i = 0; i < np.getChildCount(); i++) {
            View v = np.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            } else if (v instanceof ViewGroup) {
                setNumberPickerInput((ViewGroup) v);
            }

        }

    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

}
