package app.bxvip.com.myview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup {
    private View mLeftView;
    private View mContentView;
    private LayoutParams leftLayoutParams;
    private LayoutParams RightLayoutParams;
    private int leftWidth;
    private int rightWidth;
    private float downX;
    private float downY;
    private Scroller mScroller;
    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
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
                //按下的时候点击屏幕的位置
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //判断是要打开还是关闭
                int currentX = getScrollX();
                int middle = -mLeftView.getMeasuredWidth()/2;
                //这种做法太过突然 需要使用 scroller
                if (currentX>middle){
                    //关闭
//                    scrollTo(0,0);
                    //起始点--到结束点的过渡
                    int startX = currentX;
                    int startY = 0;
                    int endX = 0;
                    int endY = 0;
                    int dx = endX - startX;
                    int dy = endY-startY;
                    int duration = 1000;
                    mScroller.startScroll(startX,startY,dx,dy,duration);
                }else{
                    //打开
                    int startX = currentX;
                    int startY = 0;
                    int endX = -leftWidth;
                    int endY = 0;
                    int dx = endX - startX;
                    int dy = endY-startY;
                    int duration = 1000;
                    mScroller.startScroll(startX,startY,dx,dy,duration);

//                    scrollTo(-mLeftView.getMeasuredWidth(),0);
                }
               invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                int diffX = (int) (downX-moveX+0.5f);//四舍五入
                int scrollX = getScrollX()+diffX;

                Log.i("log",diffX+" === "+scrollX+"=== "+(scrollX-diffX)+" "+mLeftView.getMeasuredWidth());
                if (scrollX < -mLeftView.getMeasuredWidth()){
                    scrollTo(-mLeftView.getMeasuredWidth(),0);
                }else if(scrollX>0){
                    scrollTo(0,0);
                } else{
                    Log.i("log2",diffX+" === "+scrollX+" === "+(scrollX-diffX));
                    scrollBy(diffX, 0);
                }
//                else if (scrollX>0){
//                scrollTo(0,0);
//            }

                downX = event.getX();
                downY = event.getY();

                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }
}
