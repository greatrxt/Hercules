package com.xenodochium.hercules.application;

import android.app.Application;

import com.xenodochium.hercules.model.DaoMaster;
import com.xenodochium.hercules.model.DaoSession;

import org.greenrobot.greendao.database.Database;

public class Hercules extends Application {

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
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}