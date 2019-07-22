package app.bxvip.com.myview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SlidingMenu extends ViewGroup {
    private View mLeftView;
    private View mContentView;
    private LayoutParams leftLayoutParams;
    private LayoutParams RightLayoutParams;
    private int leftWidth;
    private int rightWidth;
    private float downX;
    private float downY;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //加载xml完成的时候回调
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftView = getChildAt(0);
        mContentView = getChildAt(1);
        leftLayoutParams = mLeftView.getLayoutParams();
        RightLayoutParams = mContentView.getLayoutParams();
        leftWidth = leftLayoutParams.width;
        rightWidth = RightLayoutParams.width;
    }

    /**
     * 继承ViewGroup必须要实现的方法
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = mLeftView.getMeasuredWidth();
        int height = mLeftView.getMeasuredHeight();
        //给左侧布局
        int lvLeft = 0;
        int lvTop = 0;
        int lvRight = width;
        int lvBottom = height;
        mLeftView.layout(-width, lvTop, 0, lvBottom);
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), lvBottom);
        //给右侧布局
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int leftMenuSpec = MeasureSpec.makeMeasureSpec(leftWidth, MeasureSpec.EXACTLY);
        int RighttMenuSpec = MeasureSpec.makeMeasureSpec(rightWidth, MeasureSpec.EXACTLY);
        mLeftView.measure(leftMenuSpec, heightMeasureSpec);
        mContentView.measure(RighttMenuSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("main",event.getAction()+"");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("main","ACTION_MOVE");
                float moveX = event.getX();
                float moveY = event.getY();
                int diffx = (int) (downX-moveX+0.5f);//四舍五入
                int scrollX = getScrollX()-diffx;

                if (scrollX<=0 && scrollX<=-mLeftView.getMeasuredWidth()){
                    //从左往右滑动
                    scrollTo(-mLeftView.getMeasuredWidth(),0);
                }else if (scrollX>0){
                    scrollTo(0,0);
                }else {
                    scrollBy(diffx, 0);
                }
                downX = moveX;
                downY= moveY;

                break;
            default:
                break;
        }
        return true;
    }
}
