package com.congtyhai.util;

import com.congtyhai.model.Realm.DTopicFirebase;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by HAI on 8/15/2017.
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController() {

        realm = Realm.getDefaultInstance();

    }

    public static RealmController getInstance() {

        if (instance == null) {

            instance = new RealmController();
        }
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public long getNextKey(Class object)
    {
        try {
            Number max = realm.where(object).max("id");
            if(max == null) {
                return  1;
            } else {
                return max.intValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e)
        { return 0; }
    }

    public void clearData(final Class object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(object);
            }

        });
    }

    public void clearAll() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }

        });
    }


    // topic
    //find all objects in the Book.class
    public RealmResults<DTopicFirebase> getTopics() {
        return realm.where(DTopicFirebase.class).findAll();
    }
}
