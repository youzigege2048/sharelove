package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "sqlbase");
        setup(schema);
        new DaoGenerator().generateAll(schema, "F://Android/ShareLove/sharelove/MyApplication/app/src/main/java-gen");
//        new DaoGenerator().generateAll(schema, "C:/CodeDevelop/jiajian/Jiajian-Android/app/src/main/java-gen");
    }

    private static void setup(Schema schema) {
        addAppInfo(schema);
    }

    private static void addAppInfo(Schema schema) {
        Entity AppInfo = schema.addEntity("AppInfo");
        AppInfo.addIdProperty();
        AppInfo.addStringProperty("TOKEN");
    }

}

