package youzi.com.sharelove;

import android.content.Intent;
import android.net.Uri;
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

import youzi.com.sharelove.modal.MusicWindow;
import youzi.com.sharelove.view.fragment_about;
import youzi.com.sharelove.view.fragment_listen;
import youzi.com.sharelove.view.fragment_main;
import youzi.com.sharelove.view.listenRoom;

import static android.widget.RadioGroup.*;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button TestBtn;

    FloatingActionButton news;
    Handler handler;

    Animation rightOut;
    boolean flag = true;

    //自定义的弹出框类
    MusicWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TestBtn = (Button) findViewById(R.id.btn);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        startActivity(new Intent(this, listenRoom.class));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.news:
                Snackbar.make(mViewPager, "Replace with your own action", Snackbar.LENGTH_LONG)
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
}