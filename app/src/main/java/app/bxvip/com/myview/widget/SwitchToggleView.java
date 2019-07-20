package app.bxvip.com.myview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import app.bxvip.com.myview.R;
import app.bxvip.com.myview.imple.MyListener;

public class SwitchToggleView extends View {
    private final static int STATE_NONE = -1;
    private final static int STATE_DOWN = 0;
    private final static int STATE_MOVE = 1;
    private final static int STATE_UP = 3;
    private MyListener listerner;
    private Bitmap mSwitchBackground;
    private Bitmap mSwitchSlide;
    private Paint mPaint = new Paint();
    private float slideX = 0;
    private float slideY = 0;
    private boolean isOpened = false; //标记滑块是否关闭
    private float viewWidth;
    private float currentX;
    private float slideWidthHalf;
    private int mState = STATE_NONE;

    public SwitchToggleView(Context context) {
        this(context, null);
    }

    public SwitchToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置背景资源
     */
    public void setSwitchBackground(int resID) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), resID);
    }

    /**
     * 设置图片资源
     */
    public void setSwitchSlide(int resID) {
        mSwitchSlide = BitmapFactory.decodeResource(getResources(), resID);
        slideWidthHalf = mSwitchSlide.getWidth() / 2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mSwitchBackground != null) {
            int width = mSwitchBackground.getWidth();
            int height = mSwitchBackground.getHeight();
            viewWidth = width;

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("=======","-------------ACTION_DOWN");
                mState = STATE_DOWN;
                //当按下的时候
                //如果滑块是关闭的
                //点击滑块的左侧是不动的
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("=======","-------------ACTION_MOVE");
                mState = STATE_MOVE;
                slideX = currentX;
                break;
            case MotionEvent.ACTION_UP:
                Log.i("=======","-------------ACTION_UP");
                mState = STATE_UP;
                if (currentX>viewWidth/2.0f){
                    isOpened = true;
                    listerner.onClick(true);
                }else {
                    isOpened = false;
                    listerner.onClick(false);
                }
                break;
            default:
                break;
        }
        Log.i("currentX  ",currentX+"'");
        invalidate();
        //默认返回的是false 这里返回true表示消费时间
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1,绘制背景
        if (mSwitchBackground != null) {
            int left = 0;
            int top = 0;
            canvas.drawBitmap(mSwitchBackground, left, top, mPaint);
        }
        //绘制滑块
        if (mSwitchSlide != null) {
            if (mState==STATE_UP){
                if (slideX<=viewWidth/2.0f){
                    canvas.drawBitmap(mSwitchSlide, 0, slideY, mPaint);
                }else{
                    canvas.drawBitmap(mSwitchSlide, viewWidth-slideWidthHalf*2f, slideY, mPaint);
                }
            }else if (mState==STATE_MOVE){
                if (currentX<=0){
                    Log.i("hongbiao","-----------7");
                    canvas.drawBitmap(mSwitchSlide, 0, slideY, mPaint);
                }else if (currentX>viewWidth-slideWidthHalf){
                    Log.i("hongbiao","-----------4");
                    canvas.drawBitmap(mSwitchSlide, viewWidth-slideWidthHalf*2, slideY, mPaint);
                }else {

                    if (slideX-slideWidthHalf<=0){
                        Log.i("hongbiao","-----------5");
                        canvas.drawBitmap(mSwitchSlide, 0, slideY, mPaint);
                    }else {
                        Log.i("hongbiao","-----------6");
                        canvas.drawBitmap(mSwitchSlide, slideX-slideWidthHalf, slideY, mPaint);
                    }
                }
            }else if (mState==STATE_DOWN){
                if (currentX<=slideWidthHalf){
                    Log.i("hongbiao","-----------1");
                    canvas.drawBitmap(mSwitchSlide, 0, slideY, mPaint);
                }else if (currentX>viewWidth-slideWidthHalf){
                    Log.i("hongbiao","-----------2");
                    canvas.drawBitmap(mSwitchSlide, viewWidth-slideWidthHalf*2, slideY, mPaint);
                }else {
                    Log.i("hongbiao","-----------3");
                    canvas.drawBitmap(mSwitchSlide, slideX-slideWidthHalf, slideY, mPaint);
                }
            }
        }
        if (mState==STATE_NONE){
            canvas.drawBitmap(mSwitchSlide, 0, slideY, mPaint);
        }
    }

    public void setOnclickListener(MyListener listerner){
        this.listerner = listerner;
    }
}
