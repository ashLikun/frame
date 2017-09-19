package com.hbung.animcheckbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/9/19　15:14
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class AnimCheckSingleHelp implements AnimCheckBox.OnCheckedChangeListener {

    List<AnimCheckBox> boxs;
    private ArrayList<OnSingleSelectListener> onSingleSelectListener = new ArrayList<>();

    public AnimCheckSingleHelp() {
    }

    public AnimCheckSingleHelp(AnimCheckBox... boxs) {
        if (boxs == null || boxs.length == 0) return;
        this.boxs = Arrays.asList(boxs);
        single();
    }

    //多个AnimChexkBox 得单选
    public void single() {
        if (boxs == null || boxs.size() <= 1) return;
        for (int i = 0; i < boxs.size(); i++) {
            AnimCheckBox item = boxs.get(i);
            item.addOnCheckedChangeListener(this);
        }
    }

    public void addAnimCheckBox(AnimCheckBox... box) {
        if (boxs == null) boxs = new ArrayList<>();
        boxs.addAll(Arrays.asList(box));
    }

    @Override
    public void onChange(AnimCheckBox checkBox, boolean checked) {
        if (checked) {
            for (int i = 0; i < boxs.size(); i++) {
                AnimCheckBox item = boxs.get(i);
                if (item != checkBox) {
                    item.setChecked(!checked);
                } else {
                    for (int j = 0; j < onSingleSelectListener.size(); j++) {
                        onSingleSelectListener.get(i).onSingleSelect(checkBox, i);
                    }
                }
            }
        }
    }


    public void clean() {
        if (boxs != null)
            boxs.clear();
    }

    public void addOnSingleSelectListener(OnSingleSelectListener onSingleSelectListener) {
        this.onSingleSelectListener.add(onSingleSelectListener);
    }

    public interface OnSingleSelectListener {
        void onSingleSelect(AnimCheckBox check, int index);
    }
}
