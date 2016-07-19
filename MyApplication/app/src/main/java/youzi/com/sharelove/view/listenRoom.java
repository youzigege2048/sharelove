package youzi.com.sharelove.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import youzi.com.sharelove.R;
import youzi.com.sharelove.modal.Config;
import youzi.com.sharelove.modal.LrcHandle;
import youzi.com.sharelove.modal.Musicinfo;
import youzi.com.sharelove.modal.TThread;
import youzi.com.sharelove.modal.WordView;
import youzi.com.sharelove.modal.internet.Form;
import youzi.com.sharelove.modal.internet.GetRoomInfo;
import youzi.com.sharelove.modal.internet.HttpMethod;

public class listenRoom extends AppCompatActivity {

    Animation Up, Down;
    Button startBtn;
    boolean is;//按钮内容控制
    boolean flag;//点击控制
    boolean isWordListenerClose;//歌词监听控制
    private WordView mWordView;
    private TextView room, num, signature, musicname;
    private List<Integer> mTimeList;
    private MediaPlayer mPlayer;
    final Handler handler = new Handler();
    Handler handlerUpdate;//更新房间信息
    Musicinfo musicinfo;//歌曲信息modal
    int progressi = -1;//歌曲进度

    String roomtoken;
    String roomId;

    boolean isclose = false;//检测当前页面是否关闭
    boolean iswordclose;//检测歌词是否关闭
    boolean isNet;//保证每次只有一个网络线程在运行
    boolean isPause;

    Thread wordThread, checkThread;//歌词监听和同步监听


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_room);

        //设置当前房间的token
        Intent i = getIntent();
        roomtoken = i.getStringExtra("token");
        roomId = i.getStringExtra("roomId");
        if (roomtoken != null && roomtoken.equals(""))
            roomtoken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjYsImlzcyI6Imh0dHA6XC9cL2FwaS5kZWJ1Zy5uZXQuY25cL3YxXC9yb29tXC9yZWdpc3RlciIsImlhdCI6MTQ2NTYxNjM0MywiZXhwIjoxNDc4NTc2MzQzLCJuYmYiOjE0NjU2MTYzNDMsImp0aSI6IjQ5NmJhODc3NWU5YmMxMGIxMGYwYTMxMjE2Y2NmOTM5In0.FG6-WSY3pgS99bMc46QcfuUB0gEhU8FpgXUGRG4UV0M";

        room = (TextView) findViewById(R.id.roomid);
        num = (TextView) findViewById(R.id.num);
        signature = (TextView) findViewById(R.id.signature);
        musicname = (TextView) findViewById(R.id.musicname);
        room.setText(roomId);

        startBtn = (Button) findViewById(R.id.startbtn);
        init();
        mWordView = (WordView) findViewById(R.id.text);
//
        musicinfo = new Musicinfo("1", "红色高跟鞋", "蔡健雅", Config.DownloadDir + "红色高跟鞋.mp3", Config.DownloadDir + "红色高跟鞋.lrc", "10000");

        num.setText(i.getStringExtra("num"));
        signature.setText(i.getStringExtra("signature"));
        musicname.setText("连接中，稍等噢~");
//        nowplay(musicinfo);
        handlerUpdate = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                num.setText("听众:" + musicinfo.num);
                signature.setText(musicinfo.signature);
                room.setText(musicinfo.id + "");
                musicname.setText(musicinfo.name + "-" + musicinfo.author);
            }
        };
        checkUpdate();
    }

    //当前播放的音乐方法，传进某首歌就立即切换
    public void nowplay(Musicinfo musicinfo) {
        if (isPause) return;//如果暂停就不播放
        if (wordThread != null) {
            wordThread.interrupt();
            iswordclose = false;
        }
        if (mPlayer != null) mPlayer.release();
        isWordListenerClose = false;
        mPlayer = new MediaPlayer();
        mPlayer.reset();
        LrcHandle lrcHandler = new LrcHandle();
        try {
            lrcHandler.readLRC(musicinfo.lrc);
            if (mTimeList != null) mTimeList.clear();
            mTimeList = lrcHandler.getTime();
            mWordView.setmWordsList(lrcHandler.getWords());
            mPlayer.setDataSource(musicinfo.dir);
            mPlayer.prepare();
            mPlayer.seekTo(Integer.parseInt(musicinfo.rate) + 800);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer.start();
        progressi = 1;
        wordListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayer != null) mPlayer.release();
            isclose = true;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //歌词监听
    public void wordListener() {
        if (isWordListenerClose) return;//如果已经打开过一个监听就不打开了
        isWordListenerClose = true;
        wordThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    iswordclose = true;
                    while (iswordclose && mPlayer != null) {
                        if (!(iswordclose && mPlayer != null) && !mPlayer.isPlaying())
                            break;
                        while (isPause) ;
                        progressi++;
                        if (progressi >= mTimeList.size())
                            progressi = mTimeList.size() - 1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (progressi > mTimeList.size() - 1) {
                                    return;
                                }
                                mWordView.invalidate();
//                            mWordView.setTime(mTimeList.get(i));
                            }
                        });
                        try {
                            while (iswordclose && mPlayer != null) {
                                if (isclose)
                                    break;
                                if ((progressi <= mTimeList.size() - 1) &&
                                        mPlayer.getCurrentPosition() < mTimeList.get(progressi)) {
                                    Thread.sleep(100);
                                } else
                                    break;
                            }
                            if (isclose)
                                break;
                            //防止重置的时候歌词处于上面的睡眠状态而进度确在初始的-1
                            if (progressi == -1)
                                continue;
                            mWordView.setPlayIndex(progressi);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isWordListenerClose = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        wordThread.start();
    }


    //初始化页面控件
    public void init() {
        startBtn.setText("暂停");
        is = true;
        isPause = false;
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
                    isNet = true;
                    isPause = true;
                    //监听暂停
                } else {
                    isNet = false;
                    isPause = false;
                    //监听继续
                    startBtn.setText("暂停");
                    //nowplay(musicinfo);
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

    //检查房间更新的线程
    public void checkUpdate() {
        isNet = false;
        checkThread = new Thread(new TimerTask() {
            @Override
            public void run() {
                try {
                    while (true) {
                        while (isPause) ;
                        if (isclose)
                            break;
                        Thread.sleep(2000);
                        updateRoom();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        checkThread.start();
    }

    public void updateRoom() {
        if (isNet) return;//每次只保证一个线程在检查
        isNet = true;
        if (isclose)
            return;
        Form form = new Form(Config.GetSongUrl + roomtoken);
        new GetRoomInfo(HttpMethod.GET, form, new GetRoomInfo.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (isclose)
                    return;
                try {
                    Musicinfo temp = GetRoomInfo.getNowMusicinfo(result);
                    System.out.println(temp.id + " - " + temp.name + " " + temp.rate + " " + temp.num);
                    if (!musicinfo.name.equals(temp.name) || Math.abs(Integer.parseInt(musicinfo.rate) - mPlayer.getCurrentPosition()) > 12000) {
                        temp.dir = Config.DownloadDir + temp.name + ".mp3";
                        temp.lrc = Config.DownloadDir + temp.name + ".lrc";
                        musicinfo = temp;
//                        System.out.println(musicinfo.id + " - " + musicinfo.name + " " + musicinfo.rate);
                        if (mPlayer != null && Math.abs(Integer.parseInt(musicinfo.rate) - mPlayer.getCurrentPosition()) < 300) {
                            musicinfo.rate = mPlayer.getCurrentPosition() + "";
                        }
                        handlerUpdate.sendEmptyMessage(0);
                        nowplay(musicinfo);
                    }
                    isNet = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new GetRoomInfo.FailCallback() {
            @Override
            public void onFail(String result) {
//                System.out.println(result);
                isNet = false;
            }
        });
    }

}
