package com.softpo.myteacyclopedia.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.LoadingLayoutProxy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.softpo.myteacyclopedia.ItemContentActivity;
import com.softpo.myteacyclopedia.R;
import com.softpo.myteacyclopedia.adapters.MyAdapter;
import com.softpo.myteacyclopedia.entitys.Tea;
import com.softpo.myteacyclopedia.utils.HttpUtils;
import com.softpo.myteacyclopedia.utils.JsonTools;

import java.util.ArrayList;
import java.util.List;

import static com.softpo.myteacyclopedia.R.id.listView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {
    private String path;

    private PullToRefreshListView mListView;
    private TextView mEmpty;

    private List<Tea> data=new ArrayList<>();
    private BaseAdapter adapter;
    private ImageView backTopImg;

    //数据页面下标
    private int page=1;
    private LoadingLayoutProxy mLoadingLayoutProxy;

    public ItemFragment() {
        // Required empty public constructor
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_item, container, false);

        initView(ret);

        initListView(data);

        initData(path+page);

        //初始化下拉刷新的布局
        initLoadingLayout();

        //长按删除
        initListViewLongLisener();

        //短按进入详情
        initListViewLisener();

        //回到顶部
        initBackTop();


        return ret;
    }

    private void initLoadingLayout() {

        mLoadingLayoutProxy= (LoadingLayoutProxy) mListView.getLoadingLayoutProxy();
        mLoadingLayoutProxy.setRefreshingLabel("正在刷新...");
    }

    //回到顶部
    private void initBackTop() {
        backTopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.getRefreshableView().setSelection(0);
            }
        });

    }

    //长按删除
    private void initListViewLongLisener() {
        mListView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

                //设置
                builder.setIcon(R.mipmap.icon_dialog);
                builder.setTitle("提示");
                builder.setMessage("真要这么绝情么？");

                builder.setNegativeButton("取消",null);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final TranslateAnimation translate = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0
                                , Animation.RELATIVE_TO_SELF, -1
                                , Animation.RELATIVE_TO_SELF, 0
                                , Animation.RELATIVE_TO_SELF, 0);
                        translate.setDuration(2000);
                        view.startAnimation(translate);
                        translate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //listView.removeView(view);
                                //data.clear();


                                data.get(0).getData().remove(position-1);

                                TranslateAnimation trans = new TranslateAnimation(
                                        Animation.RELATIVE_TO_SELF,0
                                        ,Animation.RELATIVE_TO_SELF,0
                                        ,Animation.RELATIVE_TO_SELF,1
                                        ,Animation.RELATIVE_TO_SELF,0);
                                trans.setDuration(1000);
                                int count = mListView.getRefreshableView().getChildCount();
                                int current = view.getTop();
                                for(int i=0;i<count;i++){
                                    View itemView = mListView.getRefreshableView().getChildAt(i);
                                    if(itemView.getTop()>=current) {
                                        itemView.setAnimation(trans);

                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

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
                String contentId = data.get(0).getData().get(position).getId();
                Intent intent=new Intent(getActivity(),ItemContentActivity.class);
                boolean isCollect=false;

                Bundle bundle=new Bundle();

                bundle.putString("id",contentId);
                bundle.putBoolean("isCollect",isCollect);
                intent.putExtras(bundle);

                getActivity().startActivity(intent);
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
  //                      Tea tea = JSON.parseObject(new String(bytes),Tea.class);

                        Tea tea= JsonTools.loadString(new String(bytes));

                        data.add(tea);

                        adapter.notifyDataSetChanged();

                        //刷新停止
                        if(mListView.isRefreshing()){
                            //暂停刷新
                            mListView.onRefreshComplete();

                            String time = DateUtils.formatDateTime(getContext(),
                                    System.currentTimeMillis(),
                                    DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
                            mLoadingLayoutProxy.setLastUpdatedLabel(time);
                        }

                    }


                    break;
            }
        }
    };

    private void initListView(final List<Tea> data) {
        if(data!=null){
            adapter = new MyAdapter(getContext(),data);
            mListView.setAdapter(adapter);
            mListView.setEmptyView(mEmpty);

            //设置刷新
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

                //向下拉
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    page=1;
                    data.clear();
                    initData(path+page);
                }

                //向上拉
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    page++;
                    initData(path+page);
                }
            });
            //设置刷新模式
            mListView.setMode(PullToRefreshBase.Mode.BOTH);
        }


    }

    private void initData(String paths) {
        HttpUtils.getBytes(paths,mHandler,getContext());


    }

    private void initView(View ret) {
        mListView = (PullToRefreshListView) ret.findViewById(listView);

        backTopImg = (ImageView) ret.findViewById(R.id.backTopImg);

        mEmpty = (TextView) ret.findViewById(R.id.empty);
    }

}
