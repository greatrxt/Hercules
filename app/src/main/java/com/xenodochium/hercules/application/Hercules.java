package com.xenodochium.hercules.application;

import android.app.Application;

import com.xenodochium.hercules.engine.HerculesSpeechEngine;
import com.xenodochium.hercules.model.BodyPart;
import com.xenodochium.hercules.model.DaoMaster;
import com.xenodochium.hercules.model.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        insertBasicData();
        HerculesSpeechEngine.findVoice();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void insertBasicData() {
        List<String> bodyPartsList = new ArrayList<>();
        bodyPartsList.add("Full Body");
        bodyPartsList.add("Chest");
        bodyPartsList.add("Back");
        bodyPartsList.add("Shoulders");
        bodyPartsList.add("Biceps");
        bodyPartsList.add("Triceps");
        bodyPartsList.add("Legs");

        Iterator<String> bodyPartsIterator = bodyPartsList.iterator();
        long i = 1;
        while (bodyPartsIterator.hasNext()) {
            BodyPart bodyPart = new BodyPart();
            bodyPart.setBodyPartId(i);
            bodyPart.setName(bodyPartsIterator.next());
            daoSession.getBodyPartDao().insertOrReplace(bodyPart);
            i++;
        }
    }
}