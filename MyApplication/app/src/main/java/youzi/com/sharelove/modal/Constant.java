package youzi.com.sharelove.modal;

/**
 * Created by youzi on 2016/6/10.
 */
public class Constant {
    public int CurrentState;//0初始状态
    public static int Play = 1;//1是播放
    public int Degree;//旋转角度
    public static int Pause = 2;//暂停

    Constant() {
        CurrentState = 1;
        Degree = 0;
    }
}