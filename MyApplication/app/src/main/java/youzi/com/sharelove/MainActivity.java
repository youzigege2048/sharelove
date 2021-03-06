package youzi.com.sharelove;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import youzi.com.sharelove.modal.Config;
import youzi.com.sharelove.modal.MusicWindow;
import youzi.com.sharelove.modal.internet.Form;
import youzi.com.sharelove.modal.internet.GetRoomInfo;
import youzi.com.sharelove.modal.internet.HttpMethod;
import youzi.com.sharelove.modal.sql.sqlbase.Appinfo_DbService;
import youzi.com.sharelove.view.fragment_about;
import youzi.com.sharelove.view.fragment_listen;
import youzi.com.sharelove.view.fragment_main;
import youzi.com.sharelove.view.listenRoom;

import static android.widget.RadioGroup.*;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button TestBtn;

    private ProgressDialog loadingdialog;//初始化加载

    FloatingActionButton news;
    Handler handler, startHandler;

    Animation rightOut;
    boolean flag = true;

    //自定义的弹出框类
    MusicWindow menuWindow;

    private Appinfo_DbService appinfo_dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appinfo_dbService = Appinfo_DbService.getDbService_Appinfo(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TestBtn = (Button) findViewById(R.id.btn);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        startHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        };

//        startActivity(new Intent(this, listenRoom.class));
        news = (FloatingActionButton) findViewById(R.id.news);
        news.setVisibility(View.GONE);
        news.setOnClickListener(this);
        news.setScrollBarSize(20);

        TestBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new MusicWindow(MainActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.fragment_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                if (flag) { // 第一次点击按钮生效，在消息框退出前不响应点击
                    flag = false;
                } else {
                    return;
                }

                // 显示消息框
                news.startAnimation(rightOut);
                news.setVisibility(View.VISIBLE);
            }
        });
        init();
    }

    //为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                default:
                    break;
            }
        }

    };

    public void init() {
        loadingdialog = ProgressDialog.show(this, null, "程序正在初始化，请稍候...", true, false);
        loadingdialog.onStart();
        //版本初始化
        if (appinfo_dbService.getCount() <= 0) {
            initToken(this);
        } else {
//            initToken(this);
            startHandler.sendEmptyMessage(0);
            appinfo_dbService.initToken(this.getApplication());
            //故意等待
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadingdialog.dismiss();
                }
            }).start();
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                news.setVisibility(View.INVISIBLE);
                flag = true;
                super.handleMessage(msg);
            }

        };

        rightOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0);
        rightOut.setDuration(500);

        rightOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //news.startAnimation(inDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.news:
                Snackbar.make(mViewPager, "这是个推送消息~~~", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                handler.sendEmptyMessage(0);
                return;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new fragment_main("1");
                case 1:
                    return new fragment_listen("2");
                case 2:
                    return new fragment_about();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }

    public void initToken(final Activity activity) {
        Form form = new Form(Config.RegisterTokenUrl);
        new GetRoomInfo(HttpMethod.GET, form, new GetRoomInfo.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("token" + GetRoomInfo.getRoomToken(result));
                appinfo_dbService.update_Token(GetRoomInfo.getRoomToken(result));
                appinfo_dbService.initToken(activity.getApplication());
                startHandler.sendEmptyMessage(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        loadingdialog.dismiss();
                    }
                }).start();
            }
        }, new GetRoomInfo.FailCallback() {
            @Override
            public void onFail(String result) {
                Toast.makeText(activity, "注册Token失败！-" + result, Toast.LENGTH_LONG).show();
//                finish();
            }
        });
    }
}