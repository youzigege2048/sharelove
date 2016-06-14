package youzi.com.sharelove.modal;

/**
 * Created by youzi on 2016/6/14.
 */
public class RoomInfo {
    public String id;
    public String creator;
    public String rate;
    public String signature;//个性签名
    public String num;//人数

    public RoomInfo(String id, String creator, String rate, String signature, String num) {
        this.id = id;
        this.creator = creator;
        this.rate = rate;
        this.signature = signature;
        this.num = num;
    }
}
