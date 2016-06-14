package youzi.com.sharelove.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import youzi.com.sharelove.BaseApplication;
import youzi.com.sharelove.R;
import youzi.com.sharelove.modal.Config;
import youzi.com.sharelove.modal.Constant;
import youzi.com.sharelove.modal.internet.DownloadFile;
import youzi.com.sharelove.modal.LrcHandle;
import youzi.com.sharelove.modal.MusicWindow;
import youzi.com.sharelove.modal.Musicinfo;
import youzi.com.sharelove.modal.PlayDiscView;
import youzi.com.sharelove.modal.WordView;
import youzi.com.sharelove.modal.internet.Form;
import youzi.com.sharelove.modal.internet.GetMusic;
import youzi.com.sharelove.modal.internet.GetRoomInfo;
import youzi.com.sharelove.modal.internet.HttpMethod;
import youzi.com.sharelove.modal.internet.PostBoom;
import youzi.com.sharelove.modal.internet.PostRoom;

/**
 * Created by youzi 2016/5/28.
 */
@SuppressLint("ValidFragment")
public class fragment_main extends Fragment {

    String content;
    RelativeLayout discwindows, wordwindows;
    private PlayDiscView musicPlayDiscView;

    TextView room, signature, num, progress, musicname;

    ImageView list, play, setting, nextbtn, lastbtn;

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6XC9cL2FwaS5kZWJ1Zy5uZXQuY25cL3YxXC9yb29tXC9yZWdpc3RlciIsImlhdCI6MTQ2NTkxMjg0MywiZXhwIjoxNDc4ODcyODQzLCJuYmYiOjE0NjU5MTI4NDMsImp0aSI6ImYzN2RjMTE2MGE0NDJiMmMyYTY5YzAzMzViNGU1ODYyIn0.q6aMnBnbOJp1eG3OP2CUCd9ih45QiQ8Em-JpS2QaLIQ";

    //自定义的弹出框类
    MusicWindow listWindow;

    /*
    * 播放以及歌词监听
    * */
    boolean is;//按钮内容控制
    boolean isWordListenerClose;//歌词监听控制
    private WordView mWordView;
    private List<Integer> mTimeList;
    private MediaPlayer mPlayer;
    LrcHandle lrcHandler;
    final Handler handler = new Handler();
    Handler mHandler;
    Handler mHandler2;
    Handler HandlerRdio;
    Handler handlerUpdate;//更新房间

    int nowPoint = 0;
    int roomid = 0;
    List<Musicinfo> musicinfos = new ArrayList<>();

    Musicinfo musicinfo;

    Thread wordThread, countdownThread, upSongThread;
    boolean iswordclose;
    boolean isUpSong;
    boolean isPause;//暂停控制

    public fragment_main(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //光盘窗口和歌词窗口的初始化
        discwindows = (RelativeLayout) rootView.findViewById(R.id.discwindows);
        wordwindows = (RelativeLayout) rootView.findViewById(R.id.wordwindows);
        musicPlayDiscView = new PlayDiscView(rootView.getContext());
        discwindows.addView(musicPlayDiscView);
        discwindows.setVisibility(View.INVISIBLE);
        //初始化token
        token = ((BaseApplication) this.getActivity().getApplication()).getToken();


        room = (TextView) rootView.findViewById(R.id.room);
        room.setText("23");
        num = (TextView) rootView.findViewById(R.id.num);
        progress = (TextView) rootView.findViewById(R.id.progress);
        musicname = (TextView) rootView.findViewById(R.id.musicname);
        signature = (TextView) rootView.findViewById(R.id.signature);
        list = (ImageView) rootView.findViewById(R.id.list);
        play = (ImageView) rootView.findViewById(R.id.play);
        lastbtn = (ImageView) rootView.findViewById(R.id.lastbtn);
        nextbtn = (ImageView) rootView.findViewById(R.id.nextbtn);
        setting = (ImageView) rootView.findViewById(R.id.settting);
        mWordView = (WordView) rootView.findViewById(R.id.words);
        play.setImageResource(0);
        play.setImageResource(R.drawable.rdi_btn_play);

        listWindow = new MusicWindow(rootView.getContext(), itemsOnClick);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ff = 0.6f;
                rootView.setAlpha(ff);
                //显示窗口
                listWindow.showAtLocation(rootView.findViewById(R.id.fragment_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
        listWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                float ff = 1f;
                rootView.setAlpha(ff);
            }
        });

        //初始歌曲
        mPlayer = new MediaPlayer();
        isPause = false;
        musicinfo = new Musicinfo("红色高跟鞋", "张建雅", Config.DownloadDir + "红色高跟鞋.mp3", Config.DownloadDir + "红色高跟鞋.lrc");
        nowplay(musicinfo);
        mPlayer.pause();


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if ((mPlayer.getDuration() - mPlayer.getCurrentPosition()) < 100) {
                    progress.setText("00:00");
                    return;
                }
                Date d = new Date(mPlayer.getDuration() - mPlayer.getCurrentPosition());
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                progress.setText(sdf.format(d));
            }
        };

        mHandler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                nowplay(musicinfo);
                num.setText("23");
                musicname.setText(musicinfo.name + "-" + musicinfo.author);
                signature.setText("巴拉巴拉~~");
                super.handleMessage(msg);
            }
        };

        HandlerRdio = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                play.setImageResource(0);
                play.setImageResource(R.drawable.rdi_btn_pause);
                super.handleMessage(msg);
            }
        };
        try {
            downInit();
            clickInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handlerUpdate = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                num.setText("听众:" + musicinfo.num);
                signature.setText(musicinfo.signature);
                room.setText("房间:" + roomid);
            }
        };
        updateRoom();
        return rootView;
    }

    /*
    * 音乐文件下载初始化
    * */
    public void downInit() {
        new Thread(new TimerTask() {
            @Override
            public void run() {
                final Form[] form = {new Form("http://api.debug.net.cn/v1/song/songlist")};
                new GetMusic(form[0], new GetMusic.SuccessCallback() {
                    @Override
                    public void onSuccess(String token) {
                        List<Musicinfo> temps = GetMusic.getMusicinfos(token);
                        musicinfos = temps;
                        listWindow.setMusiclist(musicinfos);
                        boolean is = false;
                        for (Musicinfo temp : temps) {
                            System.out.println(temp.dir + " " + temp.name + ".mp3");
                            System.out.println(temp.lrc + " " + temp.name + ".lrc");
//                            if (!is && DownloadFile.Download(temp.dir, temp.name + ".mp3")
//                                    && DownloadFile.DownloadTXT(temp.lrc, temp.name + ".lrc")) {
//                                musicinfo = new Musicinfo(temp.name, temp.author, Config.DownloadDir + temp.name + ".mp3", Config.DownloadDir + temp.name + ".lrc");
//                                System.out.println("xxx");
//                                handler.sendEmptyMessage(0);
//                                is = true;
//                                mHandler2.sendEmptyMessage(0);
//                            }
                        }
                    }
                }, new GetMusic.FailCallback() {
                    @Override
                    public void onFail(String token) {

                    }
                });
            }
        }).start();
    }

    //根据传进的歌曲进行切割
    public void nowplay(Musicinfo musicinfo) {
        try {
            if (wordThread != null) {
                wordThread.interrupt();
                iswordclose = false;
                countdownThread.interrupt();
            }
            System.out.println(" xxx " + musicinfo.dir);
            //if (mPlayer != null) mPlayer.release();
            isWordListenerClose = false;
            mPlayer.reset();
            lrcHandler = new LrcHandle();
            lrcHandler.readLRC(musicinfo.lrc);
            if (mTimeList != null) mTimeList.clear();
            mTimeList = lrcHandler.getTime();
            mWordView.setmWordsList(lrcHandler.getWords());
            mPlayer.setDataSource(musicinfo.dir);
            mPlayer.prepare();
//            mPlayer.seekTo(100000);
            is = false;
            play.setImageResource(0);
            play.setImageResource(R.drawable.rdi_btn_play);

            mPlayer.start();
            wordListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPlayer.start();
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            listWindow.dismiss();
        }

    };

    public void clickInit() {

        /*
        * 歌曲菜单监听
        * */
        listWindow.musiclistV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // System.out.println(id + " " + musicinfos.get((int) id).name);
                musicinfo = musicinfos.get((int) id);
                //System.out.println(musicinfo.id);
                musicinfo.dir = Config.DownloadDir + musicinfo.name + ".mp3";
                musicinfo.lrc = Config.DownloadDir + musicinfo.name + ".lrc";
                mHandler2.sendEmptyMessage(0);
                updateSongID(musicinfo.id, "0");
                is = false;
                play.performClick();
                HandlerRdio.sendEmptyMessage(0);
                listWindow.dismiss();
            }
        });

        wordwindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordwindows.setVisibility(View.INVISIBLE);
                discwindows.setVisibility(View.VISIBLE);
            }
        });
        discwindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordwindows.setVisibility(View.VISIBLE);
                discwindows.setVisibility(View.INVISIBLE);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoom();
                if (is) {
                    play.setImageResource(0);
                    play.setImageResource(R.drawable.rdi_btn_play);
                    musicPlayDiscView.constant.CurrentState = Constant.Pause;
                    mPlayer.pause();
                    //监听暂停
                    isPause = true;
                } else {
                    //监听继续
                    isPause = false;
                    play.setImageResource(0);
                    play.setImageResource(R.drawable.rdi_btn_pause);
                    musicPlayDiscView.constant.CurrentState = Constant.Play;
                    mPlayer.start();
                    if (!isWordListenerClose) {
                        wordListener();
                        isWordListenerClose = true;
                    }
                }
                is = !is;
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicinfo = musicinfos.get((++nowPoint) % musicinfos.size());
                musicinfo.dir = Config.DownloadDir + musicinfo.name + ".mp3";
                musicinfo.lrc = Config.DownloadDir + musicinfo.name + ".lrc";
                mHandler2.sendEmptyMessage(0);
                updateSongID(musicinfo.id, "0");
                is = false;
                play.performClick();
                HandlerRdio.sendEmptyMessage(0);
            }
        });
        lastbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicinfo = musicinfos.get((--nowPoint + musicinfos.size()) % musicinfos.size());
                musicinfo.dir = Config.DownloadDir + musicinfo.name + ".mp3";
                musicinfo.lrc = Config.DownloadDir + musicinfo.name + ".lrc";
                mHandler2.sendEmptyMessage(0);
                updateSongID(musicinfo.id, "0");
                is = false;
                play.performClick();
                HandlerRdio.sendEmptyMessage(0);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("请输入你要设置的个性签名");
                final EditText upBoom = new EditText(v.getContext());
                builder.setView(upBoom);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upBoom(upBoom.getText().toString());
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    /*
    * 歌词监听
    * */
    public void wordListener() {
        if (isWordListenerClose) return;//如果已经打开过一个监听就不打开了
        isWordListenerClose = true;
        wordThread = new Thread(new Runnable() {
            int i = -1;

            @Override
            public void run() {
                try {
                    iswordclose = true;
                    while (iswordclose && mPlayer != null) {
                        if (!(iswordclose && mPlayer != null) && !mPlayer.isPlaying())
                            break;
                        while (isPause) ;
                        i++;
                        if (i >= mTimeList.size())
                            i = mTimeList.size() - 1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (i > mTimeList.size() - 1) {
                                    return;
                                }
                                mWordView.invalidate();
                            }
                        });
                        try {
                            while (iswordclose && mPlayer != null) {
                                if ((i <= mTimeList.size() - 1) && (i <= mTimeList.size() - 1) &&
                                        mPlayer.getCurrentPosition() < mTimeList.get(i)) {
                                    Thread.sleep(100);
                                } else
                                    break;
                            }
                            //防止重置的时候歌词处于上面的睡眠状态而进度确在初始的-1
                            if (i == -1)
                                continue;
                            mWordView.setPlayIndex(i);
                        } catch (InterruptedException e) {
                        }
                    }
                    isWordListenerClose = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        wordThread.start();
        isUpSong = false;
        countdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (iswordclose && mPlayer != null) {
                        while (isPause) ;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(0);
                        if (mPlayer != null)
                            updateSongID(musicinfo.id, mPlayer.getCurrentPosition() + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        countdownThread.start();
    }

    public void updateSongID(String songID, String rate) {
        if (isUpSong)
            return;
        isUpSong = true;
        // System.out.println("songID - " + songID);
        Form form = new Form(Config.PostSongUrl + token);
        form.setKeyValues_post("song", songID);
        form.setKeyValues_post("rate", rate);
        new PostRoom(form, new PostRoom.SuccessCallback() {
            @Override
            public void onSuccess(String token) {
                isUpSong = false;
            }
        }, new PostRoom.FailCallback() {
            @Override
            public void onFail(String token) {
                isUpSong = false;
            }
        });
    }

    //上传个性签名
    public void upBoom(String boom) {
        Form form = new Form(Config.PostBoomUrl + token);
        form.setKeyValues_post("boom", boom);
        new PostBoom(form, new PostBoom.SuccessCallback() {
            @Override
            public void onSuccess(String token) {
                System.out.println("up boom suc!");
            }
        }, new PostBoom.FailCallback() {
            @Override
            public void onFail(String token) {
                System.out.println("up boom failed!");
            }
        });
    }

    public void updateRoom() {
        //musicinfo = new Musicinfo("红色高跟鞋", "蔡健雅", Environment.getExternalStorageDirectory() + "/Download/hongse.mp3", Environment.getExternalStorageDirectory() + "/Download/hongse.lrc");
        //handlerUpdate.sendEmptyMessage(0);
        //nowplay(musicinfo);
        Form form = new Form(Config.GetSongUrl + token);
        new GetRoomInfo(HttpMethod.GET, form, new GetRoomInfo.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Musicinfo temp = GetRoomInfo.getNowMusicinfo(result);
                    musicinfo.num = temp.num;
                    musicinfo.signature = temp.signature;
                    handlerUpdate.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new GetRoomInfo.FailCallback() {
            @Override
            public void onFail(String result) {
            }
        });
        Form form2 = new Form(Config.GetSongUrl + token);
        new GetRoomInfo(HttpMethod.GET, form2, new GetRoomInfo.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    Musicinfo temp = GetRoomInfo.getNowMusicinfo(result);
//                    System.out.println("xxxxxxxxxxxxxxxxxxxx");
                    roomid = Integer.parseInt(temp.id);
                    handlerUpdate.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new GetRoomInfo.FailCallback() {
            @Override
            public void onFail(String result) {
                System.out.println("获取房间ID错误~");
            }
        });
    }
}

