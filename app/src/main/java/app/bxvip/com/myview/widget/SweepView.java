package app.bxvip.com.myview.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import okhttp3.OkHttpClient;

/**
 * @author hb
 */
public class SweepView extends ViewGroup {
    private View mContentView;
    private View mDeleteView;
    int mDeleteViewWidth;
    private ViewDragHelper viewDragHelper;
    public SweepView(Context context) {
        this(context,null);
    }

    public SweepView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SweepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new MyCallBack());
    }

    //xml绘制完成后
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);

        LayoutParams params = mDeleteView.getLayoutParams();
        mDeleteViewWidth = params.width;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局内容区域 一般只有viewgroup才会重写这个方法
        Log.i("hongbiao",""+mContentView.getMeasuredWidth());

        mContentView.layout(0,0,mContentView.getMeasuredWidth(),mContentView.getMeasuredHeight());
        mDeleteView.layout(mContentView.getMeasuredWidth(),0,mContentView.getMeasuredWidth()+mDeleteViewWidth,mDeleteView.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mContentView.measure(widthMeasureSpec,heightMeasureSpec);
        //测量宽度值
        int mDeleteWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mDeleteViewWidth,MeasureSpec.EXACTLY);
        mDeleteView.measure(mDeleteWidthMeasureSpec,heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width,height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
     class MyCallBack extends ViewDragHelper.Callback{

        //在OntouchEvent中的down的时候调用这个方法 是否进行分析
        @Override
        public boolean tryCaptureView( View view, int i) {
            //触摸的view
            //i 表示touch的ID

            return view==mDeleteView || view==mContentView;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            Log.i("==============",""+left);
           //child表示谁移动了
            //left：child的左侧的边距
            //dx表示的是增量
            //在这里做判断是否移动 边界是不移动的
            if (child==mContentView){
                if (left>=0 ){
                    Log.i("==============","1");
                    return 0;
                }else if (-left>mDeleteViewWidth){
                    Log.i("==============","2");
                    return -mDeleteViewWidth;
                }else if (-left<mDeleteViewWidth){
                    Log.i("==============","3");
                    return left;
                }
            }else if (child==mDeleteView){
                if (left<=mContentView.getMeasuredWidth()-mDeleteViewWidth){
                    return mContentView.getMeasuredWidth()-mDeleteViewWidth;
                }else {
                    return left;
                }
            }


            //返回值 表示确定要移动多少，执行完后就开始移动
            return left;
        }

        /**
         * 当控件位置移动时回调
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //当View位置改变的时候回调数据
            //changedView表示的哪个View移动了
            //left和top表示左上角的坐标
            //dx,dy表示移动的增量
            invalidate();
            if (changedView==mContentView){
                Log.i("TAG",""+changedView.toString());
                //如果移动的内容是view
                int contentWidth = mContentView.getMeasuredWidth();
                mDeleteView.layout(contentWidth+left,0,contentWidth-left+mDeleteViewWidth,mContentView.getMeasuredHeight());
            }else if(changedView==mDeleteView){
                Log.i("hb","===="+left);
                //left表示距离屏幕左边的距离，也就是mContentView的右边距离
                int maatD = mContentView.getMeasuredWidth()-mDeleteViewWidth;
                mContentView.layout(left-mContentView.getMeasuredWidth(),0,left,mContentView.getMeasuredHeight());

            }
        }

         @Override
         public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
             super.onViewReleased(releasedChild, xvel, yvel);
             //手指抬起的时候的回调
             //releasedChild表示送开的是哪个View
             //xvel 表示x方向的加速度  yvel表示Y方向的加速度
             int left = mContentView.getLeft();
             if (-left<mDeleteViewWidth/2){
                 //关闭
                 //布局内容区域
//                 mContentView.layout(0,0,mContentView.getMeasuredWidth(),mContentView.getMeasuredHeight());
//                 mDeleteView.layout(mContentView.getMeasuredWidth(),0,mDeleteViewWidth+mContentView.getMeasuredWidth(),mDeleteView.getMeasuredHeight());
                 viewDragHelper.smoothSlideViewTo(mContentView,0,0);
                 viewDragHelper.smoothSlideViewTo(mDeleteView,mContentView.getMeasuredWidth(),0);
             }else {
                 int contentWidth = mContentView.getMeasuredWidth();
                 int contentHeight = mContentView.getMeasuredHeight();

                 //打开
                 mContentView.layout(-mDeleteViewWidth,0,mContentView.getMeasuredWidth()-mDeleteViewWidth,mContentView.getMeasuredHeight());
                 mDeleteView.layout(mContentView.getMeasuredWidth()-mDeleteViewWidth,0,mContentView.getMeasuredWidth(),mContentView.getMeasuredHeight());
                 viewDragHelper.smoothSlideViewTo(mContentView,-mDeleteViewWidth,0);
                 viewDragHelper.smoothSlideViewTo(mDeleteView,contentWidth-mDeleteViewWidth,0);
             }
//             invalidate();
             ViewCompat.postInvalidateOnAnimation(SweepView.this);
         }
     }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
