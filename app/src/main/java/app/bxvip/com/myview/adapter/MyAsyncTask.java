package app.bxvip.com.myview.adapter;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;

public class MyAsyncTask extends AsyncTask<Void,Integer,Void> {
    private ViewPager viewPager;

    public MyAsyncTask(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        int i=0;
        while (true){
            try {
                Thread.sleep(2000);
                i++;
                publishProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+values[0]);
    }
}
