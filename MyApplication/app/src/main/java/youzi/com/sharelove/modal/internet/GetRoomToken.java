package youzi.com.sharelove.modal.internet;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by youzi on 2016/6/10.
 */
public class GetRoomToken {
    public GetRoomToken(Form form, final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(form.url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    if (!obj.isNull("token")) {
                        String token = obj.getString("token");
                        if (successCallback != null)
                            successCallback.onSuccess(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (failCallback != null) {
                        failCallback.onFail("404");
                    }
                }
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
}