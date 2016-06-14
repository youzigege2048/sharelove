package youzi.com.sharelove.modal.internet;

import java.util.HashMap;

/**
 * Created by youzi on 2016/3/1.
 */
public class Form {
    String postStr;
    String url;
    HashMap<String, String> map;

    public Form(String url) {
        this.url = url;
        this.postStr = "";
        map = new HashMap<>();
    }

    public void setKeyValues_get(String key, String value) {
        this.postStr += key + "=" + value + "&";
    }

    public void setKeyValues_get2(String key, String value) {
        this.url += "?" + key + "=" + value + "&";
    }

    public void setKeyValues_post(String key, String value) {
        map.put(key, value);
    }

    public void init() {
        this.url = url;
        this.postStr = "";
        map = new HashMap<>();
    }

    public HashMap<String, String> getForm() {
        return map;
    }

    public void setKeyValues_long(String postStr) {
        this.postStr += postStr;
    }
}
