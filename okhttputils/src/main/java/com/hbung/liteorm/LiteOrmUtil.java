package com.hbung.liteorm;


import android.app.Application;

import com.hbung.okhttputils.R;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

public class LiteOrmUtil {


    private static LiteOrm liteOrm;

    private LiteOrmUtil() {
    }

    public static void init(Application application,int versionCode,boolean debugged) {
        if (liteOrm == null) {
            DataBaseConfig config = new DataBaseConfig(application, application.getString(R.string.app_name_letter));
            config.debugged = debugged; // open the log
            config.dbVersion = versionCode; // set database version
            config.onUpdateListener = null; // set database update listener
            liteOrm = LiteOrm.newSingleInstance(config);
        }
    }

    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }
}
