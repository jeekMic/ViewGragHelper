package app.bxvip.com.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.bxvip.com.myview.adapter.MyListViewAadapter;

public class MyListActivity extends AppCompatActivity {
    private ListView lv_test;
    private List<String> title;
    private MyListViewAadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        lv_test = findViewById(R.id.lv_test);
        initData();
        adapter = new MyListViewAadapter(title,this);
        lv_test.setAdapter(adapter);
    }

    private void initData() {
        title = new ArrayList<>();
        for (int i=0;i<100;i++){
            title.add("这是第 "+i+" 条数据");
        }
    }
}
