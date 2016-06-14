package youzi.com.sharelove.modal.internet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import youzi.com.sharelove.modal.Musicinfo;

/**
 * Created by youzi on 2016/6/10.
 */
public class GetMusic {
    public GetMusic(Form form, final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(form.url, HttpMethod.GET, new NetConnection.SuccessCallback() {
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
        void onSuccess(String token);
    }

    public static interface FailCallback {
        void onFail(String token);
    }

    public static ArrayList<Musicinfo> getMusicinfos(String jsonStr) {
        ArrayList<Musicinfo> musicinfos = new ArrayList<Musicinfo>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonStr);
            int iSize = jsonArray.length();
            for (int i = 0; i < iSize; i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                String author = temp.getString("author");
                String song = temp.getString("song");
                String lyric = temp.getString("lyric");
                musicinfos.add(new Musicinfo(id, name, author, song, lyric));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return musicinfos;
    }
}
