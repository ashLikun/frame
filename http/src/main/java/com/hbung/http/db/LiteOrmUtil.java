package com.hbung.http.db;


import android.app.Application;

import com.hbung.http.R;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

public class LiteOrmUtil {


    private static LiteOrm liteOrm;

    private LiteOrmUtil() {
    }

    public static void init(Application application,int versionCode) {
        if (liteOrm == null) {
            DataBaseConfig config = new DataBaseConfig(application, application.getString(R.string.app_name));
            config.debugged = true; // open the log
            config.dbVersion = versionCode; // set database version
            config.onUpdateListener = null; // set database update listener
            liteOrm = LiteOrm.newSingleInstance(config);
        }
    }

    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }
}
