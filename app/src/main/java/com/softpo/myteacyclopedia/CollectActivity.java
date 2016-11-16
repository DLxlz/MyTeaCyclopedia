package com.softpo.myteacyclopedia;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softpo.myteacyclopedia.adapters.MyCollectAdapter;
import com.softpo.myteacyclopedia.entitys.Tea;
import com.softpo.myteacyclopedia.sqlite.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mEmpty;

    //数据库
    private MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    //数据
    private List<Tea.DataBean> data=new ArrayList<>();
    private BaseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        dbHelper=new MySQLiteOpenHelper(this);
        db=dbHelper.getReadableDatabase();

        initView();

        initData();

        initListView();

        //短按进入详情
        initListViewLisener();

        //长按删除
        initListViewLongLisener();

    }

    private void initListViewLongLisener() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(CollectActivity.this);

                //设置
                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("提示");
                builder.setMessage("真要这么绝情么？");

                builder.setPositiveButton("取消",null);

                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i=0;i<data.size();i++){
                            RelativeLayout relativeLayout= (RelativeLayout) view;
                            TextView title = (TextView) relativeLayout.getChildAt(0);

                            String text = (String) title.getText();
                            if(data.get(i).getTitle().equals(text)){
                                data.remove(i);
                                adapter.notifyDataSetChanged();
                                db.delete("collectTea","title=?",new String[]{text});

                                Toast.makeText(CollectActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                    }
                });

                //目的
                builder.create().show();

                return true;
            }
        });

    }

    private void initListViewLisener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contentId = data.get(position).getId();
                Intent intent=new Intent(CollectActivity.this,ItemContentActivity.class);
                boolean isCollect=true;

                Bundle bundle=new Bundle();

                bundle.putString("id",contentId);
                bundle.putBoolean("isCollect",isCollect);
                intent.putExtras(bundle);
                CollectActivity.this.startActivity(intent);
            }
        });

    }

    private void initListView() {
        adapter = new MyCollectAdapter(this,data);

        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmpty);

    }

    private void initData() {

        Cursor cursor=db.query("collectTea",
                new String[]{"title","source","create_time","nickname","id"},
                null,null,null,null,null);

        while (cursor.moveToNext()){
            Tea.DataBean dataBean=new Tea.DataBean();

            dataBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            dataBean.setSource(cursor.getString(cursor.getColumnIndex("source")));
            dataBean.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
            dataBean.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
            dataBean.setId(cursor.getString(cursor.getColumnIndex("id")));

            data.add(dataBean);
   //         adapter.notifyDataSetChanged();
        }


    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);

        mEmpty = (TextView) findViewById(R.id.empty);
    }

    //返回上一级
    public void back(View view) {

        switch (view.getId()){
            case R.id.collectBackHome://返回home点击事件
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            case R.id.collectBack:
                this.finish();
                break;

        }

    }
}
