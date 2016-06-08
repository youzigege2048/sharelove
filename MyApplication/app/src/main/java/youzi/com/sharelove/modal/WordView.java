package youzi.com.sharelove.modal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youzi on 2016/6/5.
 */
public class WordView extends TextView {
    private List<String> mWordsList = new ArrayList<String>();
    private Paint mLoseFocusPaint;
    private Paint mOnFocusePaint;
    private float mX = 0;
    private float MiddleY = 0;
    private float mY = 0;
    private static final int DY = 50;
    private int playeIndex = 0;
    private Scroller mScroller;//滑动动画，待实现

    public WordView(Context context) throws IOException {
        super(context);
        mScroller = new Scroller(context, new LinearInterpolator());
        init();
    }

    public WordView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        mScroller = new Scroller(context, new LinearInterpolator());
        init();
    }

    public WordView(Context context, AttributeSet attrs, int defStyle)
            throws IOException {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context, new LinearInterpolator());
        init();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
//            scrollTo(0, mScroller.getCurrY());
            if (mScroller.isFinished()) {
                int cur = mScroller.getCurrX();
//                playeIndex = cur <= 1 ? 0 : cur - 1;
//                MiddleY = 0;
            }
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) return;
        if (playeIndex >= mWordsList.size())
            playeIndex = mWordsList.size() - 1;
        if (playeIndex < 0)
            return;
//        mScroller.abortAnimation();
//        mScroller.startScroll((int) mX, (int) MiddleY, (int) mX, (int) MiddleY + 2, 200);
        Paint p = mLoseFocusPaint;
        p.setTextAlign(Paint.Align.CENTER);
        Paint p2 = mOnFocusePaint;
        p2.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(mWordsList.get(playeIndex), mX, MiddleY, p2);

        float tempY = MiddleY - 10;
        int tempA = 180;
        for (int i = playeIndex - 1; i >= 0; i--) {
            tempY -= DY;
            if (tempY < 0) {
                break;
            }
            p.setAlpha((tempA -= 40));
            canvas.drawText(mWordsList.get(i), mX, tempY, p);
        }
        tempY = MiddleY + 10;
        tempA = 180;
        for (int i = playeIndex + 1, len = mWordsList.size(); i < len; i++) {
            tempY += DY;
            if (tempY > mY) {
                break;
            }
            p.setAlpha((tempA -= 15));
            canvas.drawText(mWordsList.get(i), mX, tempY, p);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        mX = w * 0.5f;
        mY = h;
        MiddleY = h * 0.3f;
        super.onSizeChanged(w, h, ow, oh);
    }

    private void init() throws IOException {
        setFocusable(true);
        mLoseFocusPaint = new Paint();
        mLoseFocusPaint.setAntiAlias(true);
        mLoseFocusPaint.setTextSize(25);
        mLoseFocusPaint.setTypeface(Typeface.SERIF);
        mLoseFocusPaint.setAlpha(130);

        mOnFocusePaint = new Paint();
        mOnFocusePaint.setAntiAlias(true);
        mOnFocusePaint.setTextSize(40);
        mOnFocusePaint.setTypeface(Typeface.SANS_SERIF);
        mOnFocusePaint.setAlpha(180);
    }

    public void setmWordsList(List<String> mWordsList) {
        this.mWordsList = mWordsList;
    }

    public void setPlayIndex(int index) {
        this.playeIndex = index;
    }

    public void setTime(long timee) {
        int time = (int) timee;
    }

}