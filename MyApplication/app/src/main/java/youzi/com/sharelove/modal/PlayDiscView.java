package youzi.com.sharelove.modal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import youzi.com.sharelove.R;

/**
 * Created by youzi  on 2016/6/10.
 */
public class PlayDiscView extends View {

    Paint paint;

    public Constant constant = new Constant();

    private Handler handler;
    // 光盘图片
    Bitmap bitmapDisc = BitmapFactory.decodeResource(getResources(),
            R.drawable.icn_play_disc);
    // 专辑图片
    Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(),
            R.drawable.playss);
    Bitmap bitmapCircularAblum, bitmapDiscCircular;
    // 光盘指针图片
    Bitmap bitmapNeedle = BitmapFactory.decodeResource(getResources(),
            R.drawable.icn_play_needle);
    WindowManager s;

    public PlayDiscView(Context context) {
        super(context);
        //分别获得光盘和专辑的圆形图片
        s = (WindowManager) this.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        bitmapImage = resizeImage(bitmapImage, 150, 150);
        bitmapCircularAblum = getCircularBitmap(bitmapImage, s.getDefaultDisplay().getHeight() / 2);
        bitmapDisc = resizeImage(bitmapDisc, 200, 200);
        bitmapDiscCircular = getCircularBitmap(bitmapDisc,
                s.getDefaultDisplay().getHeight() / 2);
        bitmapNeedle = resizeImage(bitmapNeedle, 200, 200);

        paint = new Paint();
        handler = new Handler();
        handler.post(runnable);
    }

    /**
     * 利用线程不断更新界面
     */
    private Runnable runnable = new Runnable() {
        public void run() {
            postInvalidate();
            handler.postDelayed(runnable, 50);
        }
    };

    //状态标志：
    int before = 0;
    //角度标志
    private int degreeFlag = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画光盘与专辑图片
         * 每次的旋转角度
         */
        if (constant.CurrentState == constant.Play) {
            constant.Degree++;
            if (constant.Degree > 360)
                constant.Degree = 0;

            degreeFlag = constant.Degree;

        }


        canvas.save();
        paint.setAntiAlias(true);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.rotate(constant.Degree, s.getDefaultDisplay().getWidth() / 2,
                s.getDefaultDisplay().getHeight() / 4 + 50);
        canvas.drawBitmap(bitmapCircularAblum,
                s.getDefaultDisplay().getWidth() / 2 - bitmapCircularAblum.getWidth() / 2, s.getDefaultDisplay().getHeight() / 4 + 50 - bitmapCircularAblum.getHeight() / 2, paint);

        canvas.drawBitmap(bitmapDiscCircular,
                s.getDefaultDisplay().getWidth() / 2 - bitmapDiscCircular.getWidth() / 2, s.getDefaultDisplay().getHeight() / 4 + 50 - bitmapDiscCircular.getHeight() / 2, paint);

        canvas.restore();

        /**
         * 画光盘指针图片
         */
        if (constant.CurrentState == constant.Play) {
            canvas.drawBitmap(bitmapNeedle, s.getDefaultDisplay().getWidth() / 4,
                    0, paint);

        } else {
            canvas.save();
            paint.setAntiAlias(true);
            //旋转一定角度
            canvas.rotate(80, s.getDefaultDisplay().getWidth() / 4 + 60, 0);
            canvas.drawBitmap(bitmapNeedle, s.getDefaultDisplay().getWidth() / 4,
                    0, paint);


            canvas.restore();
        }

    }

    /**
     * 获得圆形图片
     */
    private Bitmap getCircularBitmap(Bitmap bitmap, int radius) {
        Bitmap sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2,
                sbmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }

    //比例缩放图片
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }
}

