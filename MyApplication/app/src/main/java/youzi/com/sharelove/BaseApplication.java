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
        setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6XC9cL2FwaS5kZWJ1Zy5uZXQuY25cL3YxXC9yb29tXC9yZWdpc3RlciIsImlhdCI6MTQ2NTk1MjM3OSwiZXhwIjoxNDc4OTEyMzc5LCJuYmYiOjE0NjU5NTIzNzksImp0aSI6ImRiZTg2NzNjZTE1MmExZGNhODBiOTZmYWE4YzUyZjI5In0.PFI0PV5VRb7_ao5fW7XC0vRpIP5biihlO5RWwqquBzg");
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
