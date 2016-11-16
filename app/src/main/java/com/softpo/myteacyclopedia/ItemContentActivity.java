package com.softpo.myteacyclopedia;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.softpo.myteacyclopedia.entitys.TitleContent;
import com.softpo.myteacyclopedia.sqlite.MySQLiteOpenHelper;
import com.softpo.myteacyclopedia.urls.Urls;
import com.softpo.myteacyclopedia.utils.HttpUtils;

public class ItemContentActivity extends AppCompatActivity {

    private WebView mWebView;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    byte[] bytes = (byte[]) msg.obj;
                    if(bytes!=null){
                        mTitleContent = JSON.parseObject(new String(bytes),TitleContent.class);
                        //填充数据
                        initViewData(mTitleContent);
                    }
                    break;
            }
        }
    };
    private TextView mTitle;
    private TextView mSource;
    private TextView mTime;

    //数据库
    private MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private TitleContent mTitleContent;
    private LinearLayout mPresskey;

    private void initViewData(TitleContent titleContent) {
        mTitle.setText(titleContent.getData().getTitle());
        mTime.setText(titleContent.getData().getCreate_time());
        mSource.setText(titleContent.getData().getSource());
       // mWebView.loadDataWithBaseURL(null,titleContent.getData().getWeiboUrl(),null,"UTF-8",null);

        mWebView.loadUrl(titleContent.getData().getWeiboUrl());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_content);

        dbHelper=new MySQLiteOpenHelper(this);
        db=dbHelper.getReadableDatabase();

        //初始化控件
        initView();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String id = bundle.getString("id");
        boolean isCollect=bundle.getBoolean("isCollect");
        if(isCollect){
            mPresskey.setVisibility(View.GONE);
        }else {
            mPresskey.setVisibility(View.VISIBLE);
        }

        String path= Urls.CONTENT_URL+id;



        initData(path);


    }



    private void initData(String path) {
        HttpUtils.getBytes(path,mHandler);
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mTitle = (TextView) findViewById(R.id.contentTitle);
        mSource = (TextView) findViewById(R.id.sourceContent);
        mTime = (TextView) findViewById(R.id.timeContent);
        mPresskey = (LinearLayout) findViewById(R.id.pressKey);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.back:
                ItemContentActivity.this.finish();
                break;
            case R.id.collect://收藏

                ContentValues values=new ContentValues();

                values.put("title",mTitleContent.getData().getTitle());
                values.put("source",mTitleContent.getData().getSource());
                values.put("create_time",mTitleContent.getData().getCreate_time());
                values.put("nickname",mTitleContent.getData().getAuthor());
                values.put("id",mTitleContent.getData().getId());


                //判断数据是否已收藏过
                String reTitle= mTitleContent.getData().getTitle();
                reCollection(reTitle,db,values);

                break;
        }

    }

    private void reCollection(String reTitle, SQLiteDatabase db,ContentValues values) {
        Cursor cursor=db.query("collectTea",new String[]{"title","source","create_time","nickname","id"},null,null,null,null,null);
        int k=0;

        while(cursor.moveToNext()){

            String title = cursor.getString(cursor.getColumnIndex("title"));
            if(title.equals(reTitle)){
                Toast.makeText(ItemContentActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                k=1;
                break;
            }

        }
        if(k!=1){
            db.insert("collectTea",null,values);
            Toast.makeText(ItemContentActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
        }

    }


}
