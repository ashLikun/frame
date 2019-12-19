package com.ashlikun.animcheckbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/9/19　15:14
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：多个选择按钮的工具类
 */

public class AnimCheckSingleHelp implements AnimCheckBox.OnCheckedChangeListener {

    List<AnimCheckBox> boxs;
    private ArrayList<OnSingleSelectListener> onSingleSelectListener = new ArrayList<>();
    private int mSelectIndex = -1;

    public AnimCheckSingleHelp() {
    }

    public AnimCheckSingleHelp(AnimCheckBox... boxs) {
        if (boxs == null || boxs.length == 0) {
            return;
        }
        this.boxs = Arrays.asList(boxs);
        single();
    }

    //多个AnimChexkBox 得单选
    public void single() {
        if (boxs == null || boxs.size() <= 1) {
            return;
        }
        for (int i = 0; i < boxs.size(); i++) {
            AnimCheckBox item = boxs.get(i);
            item.addOnCheckedChangeListener(this);
        }
    }

    public void addAnimCheckBox(AnimCheckBox... box) {
        if (boxs == null) {
            boxs = new ArrayList<>();
        }
        boxs.addAll(Arrays.asList(box));
    }

    @Override
    public boolean onChange(AnimCheckBox checkBox, boolean checked) {
        mSelectIndex = -1;
        if (checked && getSize() > 2) {
            checkBox.setAutoSelect(false);
        }
        if (checked) {
            for (int i = 0; i < boxs.size(); i++) {
                AnimCheckBox item = boxs.get(i);
                if (item != checkBox) {
                    item.setAutoSelect(true);
                    item.setChecked(false);
                } else {
                    mSelectIndex = i;
                }
            }
        } else if (getSize() == 2) {
            for (int i = 0; i < boxs.size(); i++) {
                AnimCheckBox item = boxs.get(i);
                if (item != checkBox) {
                    item.setChecked(true);
                    mSelectIndex = i;
                }
            }
        }

        if (mSelectIndex >= 0) {
            for (int j = 0; j < onSingleSelectListener.size(); j++) {
                onSingleSelectListener.get(j).onSingleSelect(checkBox, mSelectIndex);
            }
        }
        return false;
    }


    public void clean() {
        if (boxs != null) {
            boxs.clear();
        }
    }

    public int getSize() {
        if (boxs != null) {
            return boxs.size();
        }
        return 0;
    }

    public int getSelectIndex() {
        return mSelectIndex;
    }

    public void setChecked(int index, boolean checked) {
        setChecked(index, checked, true);
    }
    public void setChecked(int index, boolean checked, boolean animation) {
        setChecked(index, checked, false, animation);
    }
    public void setCheckedNotifica(int index, boolean checked) {
        setCheckedNotifica(index, checked, true);
    }
    public void setCheckedNotifica(int index, boolean checked, boolean animation) {
        setChecked(index, checked, animation, true);
    }
    public void setChecked(int index, boolean checked, boolean isNotifica, boolean animation) {
        AnimCheckBox box = get(index);
        if (box != null) {
            box.setChecked(checked, isNotifica, animation);
        }
    }

    public AnimCheckBox get(int index) {
        if (boxs != null && boxs.size() > index) {
            return boxs.get(index);
        }
        return null;
    }

    public void addOnSingleSelectListener(OnSingleSelectListener onSingleSelectListener) {
        this.onSingleSelectListener.add(onSingleSelectListener);
    }

    public interface OnSingleSelectListener {
        void onSingleSelect(AnimCheckBox check, int index);
    }
}
