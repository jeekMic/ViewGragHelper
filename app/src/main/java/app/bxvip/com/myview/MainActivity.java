package app.bxvip.com.myview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import app.bxvip.com.myview.adapter.ViewPageAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private List<ImageView> imageViews;
    private ViewPageAdapter adapter;
    private LinearLayout mPointContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        imageViews = new ArrayList<>();
        for (int i =0 ;i<imgs.length;i++){
            iv = new ImageView(this);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(iv);

            View pointView = new View(this);

            pointView.setBackgroundResource(R.drawable.ponit_normal);
            params = new LinearLayout.LayoutParams(20,20);
            if (i!=0){
                params.leftMargin=15;
                pointView.setLayoutParams(params);
                mPointContainer.addView(pointView);
            }

        }
    }

    private void initEvent() {
            adapter = new ViewPageAdapter(imageViews,viewpager);
            viewpager.setAdapter(adapter);
    }

    private void initView() {
        viewpager = findViewById(R.id.viewpager);
        mPointContainer = findViewById(R.id.mPointContainer);

    }
}
