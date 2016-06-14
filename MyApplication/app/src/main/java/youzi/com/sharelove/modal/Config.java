package youzi.com.sharelove.modal;

import android.os.Environment;

/**
 * Created by youzi on 2016/6/11.
 */
public class Config {
    public static String DownloadDir = Environment.getExternalStorageDirectory() + "/ShareLoveDir/";
    public static String PostSongUrl = "http://api.debug.net.cn/v1/song/remotemusic?token=";
    public static String GetSongUrl = "http://api.debug.net.cn/v1/song/getsong?token=";
    public static String PostBoomUrl = "http://api.debug.net.cn/v1/room/boom?token=";
    public static String GetRoomUrl = "http://api.debug.net.cn/v1/room/roomlist";
    public static String GetRoomTokenUrl = "http://api.debug.net.cn/v1/room/join";
    public static String RegisterTokenUrl = "http://api.debug.net.cn/v1/room/register";
    public static String GetRoomInfoUrl = "http://api.debug.net.cn/v1/room/boom?token=";
    //
    public static String DB_NAME = "sldb";
}
