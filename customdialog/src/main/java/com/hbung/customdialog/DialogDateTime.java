package com.hbung.customdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hanbang.baseproject.code.base.view.activity.BaseActivity;
import com.hbung.utils.other.DateUtils;
import com.hbung.utils.ui.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/1/6 11:46
 *
 * 方法功能：时间选择对话框
 */

public class DialogDateTime extends DialogFragment {

    public static int MAX_YEAR = 3;
    public static int MIN_YEAR = -3;
    private BaseActivity context;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private CharSequence title;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private OnClickCallback onClickCallback;
    private DateTimeAdapter adapter;
    private List<DateTimeFragment> listFragment = new ArrayList<>();
    private List<String> listTitle = new ArrayList<>();
    private Calendar maxCalendar = null;
    private Calendar minCalendar = null;
    private Calendar initCalendar = null;

    private MODE mode = MODE.DATE_Y_M_D_H_M;

    public enum MODE {
        DATE_Y_M_D, DATE_Y_M_D_H_M
    }

    public static abstract class OnClickCallback {
        public abstract void onComplete(DialogDateTime dialog, Calendar content);

        public void onQuxiao(DialogDateTime dialog) {

        }
    }

    public DialogDateTime() {
        super();
    }

    @SuppressLint("ValidFragment")
    public DialogDateTime(BaseActivity context, MODE mode) {
        this.mode = mode;
        this.context = context;
    }


    public void setOnClickCallback(OnClickCallback callback) {
        onClickCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(),
                android.R.style.Theme_Holo_Light);
        View view = UiUtils.getInflaterView(contextThemeWrapper, R.layout.base_dialog_date_time);
        initView(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
        if (maxCalendar == null) {
            maxCalendar = Calendar.getInstance();
            maxCalendar.add(Calendar.YEAR, MAX_YEAR);
        }
        if (minCalendar == null) {
            minCalendar = Calendar.getInstance();
            minCalendar.add(Calendar.YEAR, MIN_YEAR);
        }
        if (initCalendar == null) {
            initCalendar = Calendar.getInstance();
            if (minCalendar != null && initCalendar.before(minCalendar)) {//最小时间之前
                initCalendar.setTime(minCalendar.getTime());
            }
            if (maxCalendar != null && initCalendar.after(maxCalendar)) {//最大时间之后
                initCalendar.setTime(maxCalendar.getTime());
            }
//            initCalendar.set(Calendar.HOUR_OF_DAY, 0);
//            initCalendar.set(Calendar.MINUTE, 0);
//            initCalendar.set(Calendar.SECOND, 0);
        }


    }

    private void init() {
        getDialog().getWindow().getAttributes().gravity = Gravity.CENTER;


         /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
//        ScreenInfoUtils screenInfoUtils = new ScreenInfoUtils(context);
//        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
//        p.width = screenInfoUtils.getWidth();
//        getWindow().setAttributes(p);
    }


    private void initView(View view) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        titleTv.setText(title);
        listTitle.add("日期");
        listTitle.add("时间");


        listFragment.add(new DateTimeFragment(1, this));
        if (mode == MODE.DATE_Y_M_D_H_M) {
            listFragment.add(new DateTimeFragment(2, this));
        } else {
            tabLayout.setVisibility(View.GONE);
        }
        viewpager.setAdapter(adapter = new DateTimeAdapter(getChildFragmentManager(), listFragment, listTitle));
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onDestroyView() {

        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }

    /**
     * 点击事件
     */
    @OnClick(value = R.id.quxiao)
    public void quxiaoOnClick(View view) {
        dismiss();
    }

    @OnClick(value = R.id.confirm)
    public void quedingOnClick(View view) {
        if (onClickCallback != null) {
            onClickCallback.onComplete(DialogDateTime.this,
                    getTime());
        }
        dismiss();
    }


    private class DateTimeAdapter extends FragmentPagerAdapter {
        private List<DateTimeFragment> listFragment;                         //fragment列表
        private List<String> listTitle;                              //tab名的列表

        public DateTimeAdapter(FragmentManager fm, List<DateTimeFragment> listFragment, List<String> listTitle) {
            super(fm);
            this.listFragment = listFragment;
            this.listTitle = listTitle;

        }

        @Override
        public DateTimeFragment getItem(int position) {
            if (listFragment.size() > position) {
                return listFragment.get(position);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return listTitle.get(position % listTitle.size());
        }
    }

    @SuppressLint("ValidFragment")
    public static class DateTimeFragment extends Fragment {

        int mode;
        DialogDateTime dialogDateTime;
        private DatePicker datePicker;
        private TimePicker timePicker;

        public DateTimeFragment() {
        }

        public DateTimeFragment(int mode, DialogDateTime dialogDateTime) {
            this.mode = mode;
            this.dialogDateTime = dialogDateTime;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (mode == 1) {
                Context contextThemeWrapper = new ContextThemeWrapper(
                        getActivity(),
                        android.R.style.Theme_Holo_Light);
                datePicker = new DatePicker(contextThemeWrapper);
                datePicker.setCalendarViewShown(false);
                initDataPicker(datePicker);
                return datePicker;
            } else {
                Context contextThemeWrapper = new ContextThemeWrapper(
                        getActivity(),
                        android.R.style.Theme_Holo_Light);
                timePicker = new TimePicker(contextThemeWrapper);
                initTimePicker(timePicker);
                return timePicker;
            }
        }

        private void initDataPicker(DatePicker datePicker) {
            DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dialogDateTime.listTitle.set(0, String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                    dialogDateTime.tabLayout.getTabAt(0).setText(dialogDateTime.listTitle.get(0));
                }
            };
            if (dialogDateTime.initCalendar != null) {
                datePicker.init(dialogDateTime.initCalendar.get(Calendar.YEAR), dialogDateTime.initCalendar.get(Calendar.MONTH), dialogDateTime.initCalendar.get(Calendar.DAY_OF_MONTH), dateChangedListener);
            } else {
                if (dialogDateTime.maxCalendar.before(Calendar.getInstance())) {
                    datePicker.init(dialogDateTime.maxCalendar.get(Calendar.YEAR), dialogDateTime.maxCalendar.get(Calendar.MONTH), dialogDateTime.maxCalendar.get(Calendar.DAY_OF_MONTH), dateChangedListener);

                }
                if (dialogDateTime.minCalendar.after(Calendar.getInstance())) {
                    datePicker.init(dialogDateTime.minCalendar.get(Calendar.YEAR), dialogDateTime.minCalendar.get(Calendar.MONTH), dialogDateTime.minCalendar.get(Calendar.DAY_OF_MONTH), dateChangedListener);
                }
            }
            try {
                datePicker.setMaxDate(dialogDateTime.maxCalendar.getTimeInMillis());
                datePicker.setMinDate(dialogDateTime.minCalendar.getTimeInMillis());
            } catch (Exception e) {

            }
            dialogDateTime.listTitle.set(0, String.format("%04d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
            dialogDateTime.tabLayout.getTabAt(0).setText(dialogDateTime.listTitle.get(0));
        }

        private void initTimePicker(TimePicker timePicker) {
            if (timePicker != null) {
                timePicker.setIs24HourView(true);
                if (dialogDateTime.initCalendar != null) {
                    timePicker.setCurrentMinute(dialogDateTime.initCalendar.get(Calendar.MINUTE));
                    timePicker.setCurrentHour(dialogDateTime.initCalendar.get(Calendar.HOUR_OF_DAY));
                } else {
                    if (dialogDateTime.maxCalendar.before(Calendar.getInstance())) {
                        timePicker.setCurrentMinute(dialogDateTime.maxCalendar.get(Calendar.MINUTE));
                        timePicker.setCurrentHour(dialogDateTime.maxCalendar.get(Calendar.HOUR_OF_DAY));
                    }
                    if (dialogDateTime.minCalendar.after(Calendar.getInstance())) {
                        timePicker.setCurrentMinute(dialogDateTime.minCalendar.get(Calendar.MINUTE));
                        timePicker.setCurrentHour(dialogDateTime.minCalendar.get(Calendar.HOUR_OF_DAY));
                    }
                }
                dialogDateTime.listTitle.set(1, String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                dialogDateTime.tabLayout.getTabAt(1).setText(dialogDateTime.listTitle.get(1));
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                        Calendar tt = dialogDateTime.getTime();
                        if (tt.before(dialogDateTime.minCalendar)) {//在最小值之前
                            view.setCurrentMinute(dialogDateTime.minCalendar.get(Calendar.MINUTE));
                            view.setCurrentHour(dialogDateTime.minCalendar.get(Calendar.HOUR_OF_DAY));
                        }
                        if (tt.after(dialogDateTime.maxCalendar)) {//在最大值之后
                            view.setCurrentMinute(dialogDateTime.maxCalendar.get(Calendar.MINUTE));
                            view.setCurrentHour(dialogDateTime.maxCalendar.get(Calendar.HOUR_OF_DAY));
                        }
                        dialogDateTime.listTitle.set(1, String.format("%02d:%02d", view.getCurrentHour(), view.getCurrentMinute()));
                        dialogDateTime.tabLayout.getTabAt(1).setText(dialogDateTime.listTitle.get(1));
                    }
                });
            }
        }

        public DatePicker getDatePicker() {
            if (mode == 1) {
                return datePicker;
            } else {
                return null;
            }
        }

        public TimePicker getTimePicker() {
            if (mode == 2) {
                return timePicker;
            } else {
                return null;
            }
        }
    }

    private DatePicker getDatePicker() {
        return adapter.getItem(0).getDatePicker();
    }

    private TimePicker getTimePicker() {
        if (adapter.getCount() < 2) {
            return null;
        }
        return adapter.getItem(1).getTimePicker();
    }

    private Calendar getTime() {
        if (getDatePicker() != null) {
            Calendar calendar = Calendar.getInstance();
            int y = getDatePicker().getYear();
            int m = getDatePicker().getMonth();
            int d = getDatePicker().getDayOfMonth();
            calendar.set(Calendar.YEAR, y);
            calendar.set(Calendar.MONTH, m);
            calendar.set(Calendar.DAY_OF_MONTH, d);
            if (getTimePicker() != null) {
                if (mode == MODE.DATE_Y_M_D_H_M) {
                    int h = getTimePicker().getCurrentHour();
                    int min = getTimePicker().getCurrentMinute();
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, min);
                    calendar.set(Calendar.SECOND, 0);
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                }
            }

            return calendar;
        }
        return null;
    }


    public Calendar getMaxCalendar() {
        return maxCalendar;
    }

    public void setMaxCalendar(Calendar maxCalendar) {
        this.maxCalendar = maxCalendar;
    }

    public void setMaxAndMinCalender(String startTime, String endTime, boolean isStart) {
        Calendar calendarMax = Calendar.getInstance();
        Calendar calendarMin = Calendar.getInstance();


        if (isStart) {
            if (TextUtils.isEmpty(endTime) || endTime.length() < 10) {
                calendarMax.add(Calendar.YEAR, MAX_YEAR);
            } else {
                calendarMax = DateUtils.stringToTime(endTime);
            }
            if (!TextUtils.isEmpty(startTime) && startTime.length() >= 10) {
                calendarMin = DateUtils.stringToTime(startTime);
            }
            calendarMin.add(Calendar.YEAR, MIN_YEAR);
        } else {
            if (!TextUtils.isEmpty(endTime) && endTime.length() >= 10) {
                calendarMax = DateUtils.stringToTime(endTime);
            }
            calendarMax.add(Calendar.YEAR, MAX_YEAR);
            if (TextUtils.isEmpty(startTime) || startTime.length() < 10) {
                calendarMin.add(Calendar.YEAR, MIN_YEAR);
            } else {
                calendarMin = DateUtils.stringToTime(startTime);
            }
        }


        maxCalendar = calendarMax;
        minCalendar = calendarMin;

    }

    public Calendar getMinCalendar() {
        return minCalendar;
    }

    public void setInitTime(Calendar calendar) {

        initCalendar = calendar;
    }

    public void setMinCalendar(Calendar minCalendar) {
        this.minCalendar = minCalendar;
    }

    /*
   * 设置标题
   */
    public DialogDateTime setTitleMain(CharSequence title) {
        this.title = title;
        return this;
    }

    public void show() {
        show(context.getSupportFragmentManager(), "");
    }

}

