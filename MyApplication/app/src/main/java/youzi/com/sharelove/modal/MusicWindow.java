package youzi.com.sharelove.modal;

/**
 * Created by youzi on 2016/6/8.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import youzi.com.sharelove.R;

public class MusicWindow extends PopupWindow {


    private Button btn_cancel;
    private View mMenuView;
    ListView musiclistV;
    WindowManager PhoneInfo;

    final String[] strs = new String[]{
            "first", "second", "third", "fourth", "fifth", "second", "third", "fourth", "fifth", "second", "third", "fourth", "fifth", "second", "third", "fourth", "fifth", "second", "third", "fourth", "fifth"
    };

    public MusicWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.selectwindow, null);
        init();
        //设置按钮监听
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高为手机高度的一半
        this.setHeight(PhoneInfo.getDefaultDisplay().getHeight() / 2);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.anim_menu_bottombar);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                /*触电不在弹窗就关闭弹窗*/
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    void init() {
        /*
        * 获取设备窗体信息
        * */
        PhoneInfo = (WindowManager) mMenuView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);


        /*
        * 初始化音乐列表
        * */
        musiclistV = (ListView) mMenuView.findViewById(R.id.listV);
        musiclistV.setAdapter(new ArrayAdapter<String>(mMenuView.getContext(),
                android.R.layout.simple_list_item_1, strs));

    }

}
