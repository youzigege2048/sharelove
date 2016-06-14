package youzi.com.sharelove.modal.sql.sqlbase;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import youzi.com.sharelove.BaseApplication;

/**
 * Created by youzi on 2016/5/2.
 */
public class Appinfo_DbService {
    private static Context appContext;
    private static Appinfo_DbService intance;
    private SQLiteDatabase db;
    private DaoSession mDaoSession;
    private AppInfoDao appInfoDao;
    private BaseApplication baseApplication;

    public static Appinfo_DbService getDbService_Appinfo(Context context) {
        if (intance == null) {
            intance = new Appinfo_DbService();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            intance.mDaoSession = BaseApplication.getDaoSession(context);
            intance.appInfoDao = intance.mDaoSession.getAppInfoDao();
        }
        return intance;
    }

    public void setToken(Application appContext, String token) {
        baseApplication = (BaseApplication) appContext;
        baseApplication.setToken(token);
    }

    /*
    * 将Token全局化
    * */
    public void initToken(Application appContext) {
        baseApplication = (BaseApplication) appContext;
        AppInfo appInfo = appInfoDao.load(1l);
//        System.out.println("appinfo - " + appInfo.getTOKEN());
        baseApplication.setToken(appInfo.getTOKEN());
    }


    public void update_Token(String token) {
        AppInfo appInfo = appInfoDao.load(1l);
        if (appInfo == null)
            appInfo = new AppInfo();
        System.out.println(token);
        appInfo.setTOKEN(token);
        appInfoDao.insertOrReplace(appInfo);
    }

    public long getCount() {
        return appInfoDao.count();
    }

    public void saveAppInfo(AppInfo appInfo) {
        appInfoDao.insertOrReplace(appInfo);
    }

    public void deleteAll() {
        appInfoDao.deleteAll();
    }

}


