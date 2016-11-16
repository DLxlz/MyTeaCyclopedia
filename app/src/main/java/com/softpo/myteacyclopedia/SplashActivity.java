package com.softpo.myteacyclopedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView time;
    private int currentTime=3;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 3:
                    currentTime--;
                    if(currentTime==0){
                        SharedPreferences sp=getSharedPreferences("loadRecord",MODE_PRIVATE);
                        //判断是否是第一次
                        boolean isFirst=sp.getBoolean("isFirst",true);

                        if(isFirst){
                            Intent intent=new Intent(SplashActivity.this,GuidActivity.class);
                            SplashActivity.this.startActivity(intent);
                        }else {
                            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                            SplashActivity.this.startActivity(intent);
                        }

                    }
                    time.setText(""+currentTime);
                    this.sendEmptyMessageDelayed(3,1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //隐藏ActionBar
        getSupportActionBar().hide();

        initView();

        initTextView();

    }

    private void initTextView() {
        Message msg = Message.obtain();
        msg.what=3;

        mHandler.sendEmptyMessageDelayed(3,1000);
    }

    private void initView() {
        time = (TextView) findViewById(R.id.time);
        time.setText("3");
    }
}
