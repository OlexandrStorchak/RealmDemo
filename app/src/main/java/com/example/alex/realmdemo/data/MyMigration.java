package com.example.alex.realmdemo.data;

import javax.xml.validation.Schema;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;

/**
 * Created by Alex on 12.12.2016.
 */

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion == 1) {
            RealmObjectSchema user = realm.getSchema().get("User");
            user.addField("searchName", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setString("searchName", obj.getString("name").toLowerCase());
                        }
                    });
            oldVersion++;
        }
    }
}
