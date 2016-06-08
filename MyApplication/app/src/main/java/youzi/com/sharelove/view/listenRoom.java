package youzi.com.sharelove.view;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import youzi.com.sharelove.R;
import youzi.com.sharelove.modal.LrcHandle;
import youzi.com.sharelove.modal.WordView;

public class listenRoom extends AppCompatActivity {

    Animation Up, Down;
    Button startBtn;
    boolean is;//按钮内容控制
    boolean flag;//点击控制
    boolean isWordListenerClose;//歌词监听控制


    private WordView mWordView;
    private List<Integer> mTimeList;
    private MediaPlayer mPlayer;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_room);
        startBtn = (Button) findViewById(R.id.startbtn);
        init();
        mWordView = (WordView) findViewById(R.id.text);
        isWordListenerClose = false;

        mPlayer = new MediaPlayer();
        mPlayer.reset();
        LrcHandle lrcHandler = new LrcHandle();
        try {
            lrcHandler.readLRC(Environment.getExternalStorageDirectory() + "/Download/hongse.lrc");
            mTimeList = lrcHandler.getTime();
            mWordView.setmWordsList(lrcHandler.getWords());
            mPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/Download/hongse.mp3");
            mPlayer.prepare();
            mPlayer.seekTo(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPlayer.start();
        wordListener();
    }


    public void wordListener() {
        if (isWordListenerClose) return;//如果已经打开过一个监听就不打开了
        isWordListenerClose = true;
        new Thread(new Runnable() {
            int i = -1;

            @Override
            public void run() {
                while (mPlayer.isPlaying()) {
                    i++;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (i > mTimeList.size() - 1) {
                                return;
                            }
                            mWordView.invalidate();
//                            mWordView.setTime(mTimeList.get(i));
                        }
                    });
                    try {
                        while ((i <= mTimeList.size() - 1) && mPlayer.getCurrentPosition() < mTimeList.get(i)) {
                            Thread.sleep(100);
                        }
                        mWordView.setPlayIndex(i);
                    } catch (InterruptedException e) {
                    }
                }
                isWordListenerClose = false;
            }
        }).start();
    }


    public void init() {
        startBtn.setText("暂停");
        is = true;
        flag = true;
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                } else
                    return;
                startBtn.startAnimation(Up);
            }
        });
        Up = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        Up.setDuration(200);
        Down = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF,
                0);
        Down.setDuration(200);
        Up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startBtn.startAnimation(Down);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (is) {
                    startBtn.setText("继续");
                    mPlayer.pause();
                } else {
                    startBtn.setText("暂停");
                    mPlayer.start();
                    if (!isWordListenerClose) {
                        wordListener();
                        isWordListenerClose = true;
                    }
                }
                is = !is;
                flag = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
