package app.bxvip.com.myview;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.bxvip.com.myview.adapter.ViewPageAdapter;
import app.bxvip.com.myview.imple.GetRequest_Interface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";
    private ViewPager viewpager;
    private TextView title;
    private List<ImageView> imageViews;
    private ViewPageAdapter adapter;
    private LinearLayout mPointContainer;
    private List<String> titles;
    public  WeakReferenceHandler handler = new WeakReferenceHandler(this);
    private MyThread thread;
    private static int add = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        //初始化View
        initView();
        //初始化数据
        initData();
        //初始化事件
        initEvent();

    }

    private void initData() {
        ImageView iv;
        LinearLayout.LayoutParams params;
        int[] imgs = {R.drawable.icon_1,R.drawable.icon_2,R.drawable.icon_3,R.drawable.icon_4,R.drawable.icon_5,};
        titles = Arrays.asList("黄鹤楼送孟浩然之广陵", "故人西辞黄鹤楼", "烟花三月下扬州", "孤帆远影碧空尽", "唯见长江天际流");
        imageViews = new ArrayList<>();
        for (int i =0 ;i<imgs.length;i++){
            iv = new ImageView(this);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(iv);
            View pointView = new View(this);
            if (i==0){
                pointView.setBackgroundResource(R.drawable.point_select);
            }else {
                pointView.setBackgroundResource(R.drawable.ponit_normal);
            }
            params = new LinearLayout.LayoutParams(20,20);
            if (i!=0){
                params.leftMargin=15;
            }

            pointView.setLayoutParams(params);
            mPointContainer.addView(pointView);

        }

    }

    private void initEvent() {
            adapter = new ViewPageAdapter(imageViews,viewpager);
            viewpager.setAdapter(adapter);
            viewpager.addOnPageChangeListener(this);
            title.setText(titles.get(0));
             //设置默认选择中间值
             int middle = Integer.MAX_VALUE/2;
             int temp = middle%5;
             int item = middle-temp+add;
             viewpager.setCurrentItem(item);
            thread = new MyThread(handler);
            thread.start();

    }

    private void initView() {
        viewpager = findViewById(R.id.viewpager);
        title = findViewById(R.id.title);
        mPointContainer = findViewById(R.id.mPointContainer);


    }

    /**
     * viewpager滚动时的回调
     * @param i position
     * @param v positionOffset 滑动的百分比
     * @param i1  positionOffsetPixels:偏移的距离 这个跟显示屏的分辨率
     * 当positionOffsetsetPixes超过了屏幕的宽度的时候，position就会改变
     */
    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Log.i("MainActivity",i+"  "+v+"  "+i1);
    }

    /**选中了某个页面的回调
     * 第一次显示的时候不回调，改变的时候才回调
     * @param i
     */
    @Override
    public void onPageSelected(int i) {
        Log.i("onPageSelected        ",i+"");
        //在此方法中改变点的切换
        setSelect(i%5);
    }

    /**
     * 滑动状态改变的回调
     * 1 表示拖动
     * 2 表示固定
     * 0 表示闲置
     * @param i
     */
    @Override
    public void onPageScrollStateChanged(int i) {
        Log.i("onPageScrollStateChanged        ",i+"");
    }


    public void setSelect(int select){
        int num = mPointContainer.getChildCount();
        title.setText(titles.get(select));
        for (int i=0;i< num;i++){
            if (i==select){
                mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.point_select);
            }else {
                mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.ponit_normal);
            }
        }
    }

    public void onChange(View view) {
        startActivity(new Intent(this, OtherActivity.class));
    }

    static class WeakReferenceHandler extends Handler{
        private WeakReference<MainActivity> mWeakReference;

        public WeakReferenceHandler(MainActivity activity) {
            this.mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            int currentIndex =  mWeakReference.get().viewpager.getCurrentItem()+msg.arg1;
            Log.i("currentItem","  "+currentIndex);
            mWeakReference.get().viewpager.setCurrentItem(currentIndex);
        }
    }
    static class MyThread extends Thread{
        private WeakReferenceHandler handler;
        private int i = 1;
        public MyThread(WeakReferenceHandler handler) {
            this.handler = handler;
            this.i = 1;
        }

        @Override
        public void run() {
            Message message;
            while (true){
                try {
                    sleep(2000);
                   message = Message.obtain();
                   message.arg1 = i;
                    add++;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    Log.i("TAG","线程已中断");
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG","onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当activity销毁时中断线程防止内存泄漏
        thread.interrupt();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAG","onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    public void netRequest(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://fanyi.youdao.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
//        Call<Reception> call = request.getCall("");
//        call.enqueue(new Callback<Reception>() {
//            @Override
//            public void onResponse(Call<Reception> call, Response<Reception> response) {
//                response.body().show();
//            }
//
//            @Override
//            public void onFailure(Call<Reception> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG,"调用");
    }
}
