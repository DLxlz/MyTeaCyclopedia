package com.softpo.myteacyclopedia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softpo.myteacyclopedia.adapters.MyAdapter;
import com.softpo.myteacyclopedia.entitys.Tea;
import com.softpo.myteacyclopedia.urls.Urls;
import com.softpo.myteacyclopedia.utils.HttpUtils;
import com.softpo.myteacyclopedia.utils.JsonTools;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mEmpty;

    private List<Tea> data=new ArrayList<>();
    private BaseAdapter adapter;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //隐藏ActionBar
        getSupportActionBar().hide();

        String search = getIntent().getStringExtra("search");
        String path= Urls.SEARCH_URL+search;

        Log.d("flag", "----------->onCreate:" +search);

        initView(search);

        initData(path);

        initListViewLisener();

        //长按删除
        initListViewLongLisener();


    }

    private void initListViewLongLisener() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(SearchActivity.this);

                //设置
                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("提示");
                builder.setMessage("真要这么绝情么？");

                builder.setNegativeButton("取消",null);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i=0;i<data.size();i++){
                            RelativeLayout relativeLayout= (RelativeLayout) view;
                            TextView title = (TextView) relativeLayout.getChildAt(1);

                            String text = (String) title.getText();
                            if(data.get(0).getData().get(i).getTitle().equals(text)){
                                data.remove(i);
                                adapter.notifyDataSetChanged();

                                break;
                            }
                        }

                    }
                });

                //目的
                builder.create().show();

                return false;
            }
        });



    }

    private void initListViewLisener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contentId = data.get(0).getData().get(position).getId();
                Intent intent=new Intent(SearchActivity.this,ItemContentActivity.class);

                boolean isCollect=false;

                Bundle bundle=new Bundle();

                bundle.putString("id",contentId);
                bundle.putBoolean("isCollect",isCollect);
                intent.putExtras(bundle);

                SearchActivity.this.startActivity(intent);
            }
        });

    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    byte[] bytes= (byte[]) msg.obj;

                    if (bytes != null) {
                        //Tea tea = JSON.parseObject(new String(bytes),Tea.class);

                        Tea tea= JsonTools.loadString(new String(bytes));

                        data.add(tea);
                        initListView(data);
                    }


                    break;
            }
        }
    };

    private void initListView(List<Tea> data) {
        if(data!=null){
            adapter = new MyAdapter(this,data);
            mListView.setAdapter(adapter);
            mListView.setEmptyView(mEmpty);
        }


    }

    private void initData(String path) {
        HttpUtils.getBytes(path,mHandler);



    }

    private void initView(String search) {
        mListView = (ListView) findViewById(R.id.listView);
        mEmpty = (TextView) findViewById(R.id.empty);

        mTextView = (TextView) findViewById(R.id.searchText);
        mTextView.setText(search);
    }

    public void back(View view) {
        this.finish();
    }
}
