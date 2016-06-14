package youzi.com.sharelove;

import android.app.Application;
import android.content.Context;

import youzi.com.sharelove.modal.Config;
import youzi.com.sharelove.modal.sql.sqlbase.DaoMaster;
import youzi.com.sharelove.modal.sql.sqlbase.DaoSession;

/**
 * Created by youzi on 2016/6/10.
 */
public class BaseApplication extends Application {
    private static BaseApplication bintance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private String token;

    public void onCreate() {
        super.onCreate();
        if (bintance == null)
            bintance = this;
        //默认值
        setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjYsImlzcyI6Imh0dHA6XC9cL2FwaS5kZWJ1Zy5uZXQuY25cL3YxXC9yb29tXC9yZWdpc3RlciIsImlhdCI6MTQ2NTYxNjM0MywiZXhwIjoxNDc4NTc2MzQzLCJuYmYiOjE0NjU2MTYzNDMsImp0aSI6IjQ5NmJhODc3NWU5YmMxMGIxMGYwYTMxMjE2Y2NmOTM5In0.FG6-WSY3pgS99bMc46QcfuUB0gEhU8FpgXUGRG4UV0M");
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(context, Config.DB_NAME, null);
            daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
