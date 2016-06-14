package youzi.com.sharelove.modal.internet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import youzi.com.sharelove.modal.Musicinfo;
import youzi.com.sharelove.modal.RoomInfo;

/**
 * Created by youzi on 2016/6/11.
 */
public class GetRoomInfo {
    public GetRoomInfo(HttpMethod method, Form form, final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(form.url, method, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (successCallback != null)
                    successCallback.onSuccess(result);
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail(String statuscode) {
                if (failCallback != null) {
                    failCallback.onFail("404");
                }
            }
        }, form);
    }

    public static interface SuccessCallback {
        void onSuccess(String result);
    }

    public static interface FailCallback {
        void onFail(String result);
    }

    public static Musicinfo getNowMusicinfo(String result) {
        JSONObject temp = null;
        Musicinfo musicinfo = null;
        try {
            temp = new JSONObject(result);
            String id = temp.getString("id");
            String name = temp.getString("name");
            String author = temp.getString("author");
            String song = temp.getString("song");
            String lyric = temp.getString("lyric");
            String rate = temp.getString("rate");
            String num = temp.getString("num");
            String signature = temp.getString("boom");
            musicinfo = new Musicinfo(id, name, author, song, lyric, rate, num, signature);
        } catch (JSONException e) {
            e.printStackTrace();
            musicinfo = new Musicinfo("0", "0", "0", "", "");
        }
        return musicinfo;
    }

    public static ArrayList<RoomInfo> getRooms(String jsonStr) {
        ArrayList<RoomInfo> roomInfos = new ArrayList<RoomInfo>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonStr);
            int iSize = jsonArray.length();
            for (int i = 0; i < iSize; i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                String id = temp.getString("id");
                String song = temp.getString("song");
                String creator = temp.getString("creator");
                String rate = temp.getString("rate");
                String signature = temp.getString("boom");
                String num = temp.getString("num");
                roomInfos.add(new RoomInfo(id, creator, rate, signature, num));// RoomInfo(String id, String song, String rate, String signature, String num) {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return roomInfos;
    }

    public static String getRooms2(String jsonStr) {
        ArrayList<RoomInfo> roomInfos = new ArrayList<RoomInfo>();
        JSONObject temp = null;
        try {
            temp = new JSONObject(jsonStr);
            String id = temp.getString("id");
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRoomToken(String jsonStr) {
        ArrayList<RoomInfo> roomInfos = new ArrayList<RoomInfo>();
        JSONObject temp = null;
        try {
            temp = new JSONObject(jsonStr);
            String token = temp.getString("token");
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}