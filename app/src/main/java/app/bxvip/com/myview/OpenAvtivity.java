package app.bxvip.com.myview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.bxvip.com.myview.imple.MyListener;
import app.bxvip.com.myview.widget.SwitchToggleView;

public class OpenAvtivity extends AppCompatActivity {
    private SwitchToggleView  mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_avtivity);
        mView = findViewById(R.id.switchView);
        mView.setSwitchBackground(R.drawable.switch_background);
        mView.setSwitchSlide(R.drawable.slide_button_background);
        mView.setOnclickListener(new MyListener() {
            @Override
            public String onClick(boolean isOpen) {
                Toast.makeText(OpenAvtivity.this, ""+isOpen, Toast.LENGTH_SHORT).show();
                return "hello";
            }
        });
    }
}
