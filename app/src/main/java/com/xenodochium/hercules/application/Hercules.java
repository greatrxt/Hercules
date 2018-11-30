package com.xenodochium.hercules.application;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import com.wooplr.spotlight.SpotlightConfig;
import com.xenodochium.hercules.R;
import com.xenodochium.hercules.engine.HerculesSpeechEngine;
import com.xenodochium.hercules.model.DaoMaster;
import com.xenodochium.hercules.model.DaoSession;
import com.xenodochium.hercules.util.InitialData;

import org.greenrobot.greendao.database.Database;

public class Hercules extends Application {

    public static final String TAG = "Hercules";
    private static Hercules instance;
    private DaoSession daoSession;

    public static Hercules getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "hercules");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();
        InitialData.insertBasicDataIfFirstTime(daoSession);
        HerculesSpeechEngine.findMaleVoice();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * Remove control panel on notification bar
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * Spotlight for help when user opens app for first time
     * @return
     */
    public static SpotlightConfig getSpotlightConfig(){
        SpotlightConfig spotlightConfig = new SpotlightConfig();
        spotlightConfig.setIntroAnimationDuration(400);
        spotlightConfig.setRevealAnimationEnabled(true);
        spotlightConfig.setPerformClick(true);
        spotlightConfig.setFadingTextDuration(400);
        spotlightConfig.setHeadingTvColor(getInstance().getResources().getColor(R.color.white));
        spotlightConfig.setHeadingTvSize(32);
        //spotlightConfig.setHeadingTvText("some text");
        spotlightConfig.setSubHeadingTvColor(getInstance().getResources().getColor(R.color.colorPrimaryDark));
        spotlightConfig.setSubHeadingTvSize(16);
        //spotlightConfig.setSubHeadingTvText("Like the picture?\nLet others know.");
        spotlightConfig.setMaskColor(Color.parseColor("#dc000000"));
        spotlightConfig.setLineAnimationDuration(400);
        spotlightConfig.setLineAndArcColor(getInstance().getResources().getColor(R.color.colorPrimary));
        spotlightConfig.setDismissOnTouch(true);
        spotlightConfig.setDismissOnBackpress(true);
        return spotlightConfig;
    }
}
