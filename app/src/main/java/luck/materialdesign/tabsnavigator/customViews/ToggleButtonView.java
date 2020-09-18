package luck.materialdesign.tabsnavigator.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import luck.materialdesign.tabsnavigator.R;


public class ToggleButtonView extends View implements View.OnClickListener {
    private Bitmap switchBackgroundBitmap;
    private Bitmap switchForegroundBitmap;
    private Paint paint;
    private int slidLeftMax;
    private int slidLeft;

    public boolean isSwitchOn() {
        return switchOn;
    }

    private static boolean switchOn;
    private float startX;
    private float endX;
    private float distanceX;
    private float initialX;
    private boolean isClickEnabled;

    public ToggleButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        switchOn = false;
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        Bitmap background  = BitmapFactory.decodeResource(getResources(), R.drawable.swith_background);
        Bitmap foreground = BitmapFactory.decodeResource(getResources(), R.drawable.switch_foreground);
        switchBackgroundBitmap = Bitmap.createScaledBitmap(background, displaymetrics.widthPixels/2,
                displaymetrics.heightPixels/13, false);
        switchForegroundBitmap = Bitmap.createScaledBitmap(foreground, displaymetrics.widthPixels/4,
                displaymetrics.heightPixels/13, false);

        slidLeftMax = switchBackgroundBitmap.getWidth() - switchForegroundBitmap.getWidth();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(switchForegroundBitmap, slidLeft, 0, paint);
    }

    @Override
    public void onClick(View view) {
        if (isClickEnabled) {
            switchOn = !switchOn;
            refreshView();
        }
    }


    private void refreshView() {
        if (switchOn) {
            slidLeft = slidLeftMax;
        } else {
            slidLeft = 0;
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClickEnabled = true;
                initialX = event.getX();
                startX = event.getX();
                break;            
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                distanceX = endX - startX;
                slidLeft += distanceX;
                if (slidLeft < 0) {
                    slidLeft = 0;
                } else if (slidLeft > slidLeftMax) {
                    slidLeft = slidLeftMax;
                }
                invalidate();
                startX = event.getX();
                break;            
            case MotionEvent.ACTION_UP:
                if (slidLeft < slidLeftMax / 2) {
                    switchOn = false;
                } else if(slidLeft >= slidLeftMax / 2) {
                    switchOn = true;
                }
                isClickEnabled = Math.abs(endX - initialX) <= 5;
                refreshView();
                break;
            
        }
        return true;
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();

    }


}
