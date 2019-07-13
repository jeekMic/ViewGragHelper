package app.bxvip.com.myview.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    private List<ImageView> images;
    private ViewPager viewPager;

    public ViewPageAdapter(List<ImageView> images,ViewPager viewPager) {
        this.images = images;
        this.viewPager = viewPager;
    }

    //返回几个item
    @Override
    public int getCount() {
        return images.size();
    }
    //标记方法，用来判断缓存标记
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        //view表示正在显示的View
        return view==o;
    }

    //初始化item
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //添加要显示的View
        ImageView imageView = images.get(position);
        viewPager.addView(imageView);
        //记录缓存标记
        return imageView;
    }
    //销毁item条目
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //object就是标记
        ImageView imageView = images.get(position);
        viewPager.removeView(imageView);
    }
}
