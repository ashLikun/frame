package com.hbung.utils.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 10:47
 * <p>
 * 方法功能：activity的操作类
 */

public class ActivityUtils {


    /**
     * Get AppCompatActivity from context
     *
     * @param context
     * @return AppCompatActivity if it's not null
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context 某个界面名称
     */
    public static boolean isForeground(Activity context) {
        if (context instanceof Activity) {
            String className = context.getClass().getName();
            if (context == null || TextUtils.isEmpty(className)) {
                return false;
            }

            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            if (list != null && list.size() > 0) {
                ComponentName cpn = list.get(0).topActivity;
                if (className.equals(cpn.getClassName())) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 判断应用是否处于前台
     *
     * @param context 某个界面名称
     */
    public static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if (runningTasks != null) {
            ActivityManager.RunningTaskInfo foregroundTaskInfo = runningTasks.get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            if (context.getPackageName().equals(foregroundTaskPackageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 把栈顶activity切换到前台，如果应用未启动就打开应
     *
     * @return 0：前台 1:处于后台  2：未启动或者被回收
     */
    public static int appBackgoundToForeground(Context context) {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        if (taskList.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
            return 0;//前台
        }
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                mAm.moveTaskToFront(rti.id, 0);
                return 1;//后台
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity

        return 2;//未启动
    }
    /**
     * public static boolean isBackground(Context context) {

     Log.d("Nat: isBackground.packageName1", context.getPackageName());

     ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

     List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

     for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

     Log.d("Nat: isBackground.processName", appProcess.processName);

     if (appProcess.processName.equals(context.getPackageName())) {

     Log.d("Nat: isBackground.importance", String.valueOf(appProcess.importance));

     if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {

     Log.i("后台", appProcess.processName);

     return true;

     }else{

     Log.i("前台", appProcess.processName);

     return false;

     }

     }

     }

     return false;

     }
     */

    /**
     * class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
    @Override protected Boolean doInBackground(Context... params) {
    final Context context = params[0].getApplicationContext();
    return isAppOnForeground(context);
    }
    private boolean isAppOnForeground(Context context) {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
    if (appProcesses == null) {
    return false;
    }
    final String packageName = context.getPackageName();
    Log.d("Nat: isAppOnForeground.packageName", context.getPackageName());
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
    Log.d("Nat: isAppOnForeground.processName", appProcess.processName);
    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
    return true;
    }
    }
    return false;
    }
    }
     */
}
