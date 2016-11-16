package com.softpo.myteacyclopedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.softpo.myteacyclopedia.adapters.GuidPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuidActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    int position = (int) msg.obj;
                    if(position==2){
                        Intent intent=new Intent(GuidActivity.this,MainActivity.class);
                        GuidActivity.this.startActivity(intent);
                    }

                    break;
            }
        }
    };
    private ImageView mPoint1;
    private ImageView mPoint2;
    private ImageView mPoint3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);

        //隐藏Actionbar
        getSupportActionBar().hide();

        //记录用户体验
        initRecord();

        initView();

        initViewPager();

        //设置滑动监听
        initViewPagerLisener();

        //设置初始状态
        mPoint1.setEnabled(true);
        mPoint2.setEnabled(false);
        mPoint3.setEnabled(false);

    }

    private void initRecord() {
        SharedPreferences sp=getSharedPreferences("loadRecord",MODE_PRIVATE);

        SharedPreferences.Editor editor=sp.edit();

        editor.putBoolean("isFirst",false);

        editor.commit();

    }

    private void initViewPagerLisener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    mPoint1.setEnabled(false);
                    mPoint2.setEnabled(false);
                    mPoint3.setEnabled(true);

                }else if(position==1){
                    mPoint1.setEnabled(false);
                    mPoint2.setEnabled(true);
                    mPoint3.setEnabled(false);
                }

                Message msg = Message.obtain();
                msg.what=11;
                msg.obj=position;
                mHandler.sendMessageDelayed(msg,1000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void initViewPager() {
        //设置图片资源
        List<ImageView> data=new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ImageView imageView=new ImageView(this);
            switch(i){
                case 0:
                    imageView.setImageResource(R.mipmap.slide1);
                    break;
                case 1:
                    imageView.setImageResource(R.mipmap.slide2);
                    break;
                case 2:
                    imageView.setImageResource(R.mipmap.slide3);
                    break;
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            data.add(imageView);

        }

        PagerAdapter adapter=new GuidPagerAdapter(data);

        mViewPager.setAdapter(adapter);


    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mPoint1 = (ImageView) findViewById(R.id.currenPoint1);
        mPoint2 = (ImageView) findViewById(R.id.currenPoint2);
        mPoint3 = (ImageView) findViewById(R.id.currenPoint3);

    }
}
