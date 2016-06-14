package youzi.com.sharelove.modal.internet;

/**
 * Created by youzi on 2016/6/12.
 */
public class PostBoom {
    public PostBoom(Form form, final SuccessCallback successCallback, final FailCallback failCallback) {
        new NetConnection(form.url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (successCallback != null)
                    successCallback.onSuccess("OK");
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