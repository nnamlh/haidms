package com.congtyhai.model.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by HAI on 9/19/2017.
 */

public class DTopicFirebase extends RealmObject {

    @Required
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
