package com.softpo.myteacyclopedia.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.LoadingLayoutProxy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.softpo.myteacyclopedia.ItemContentActivity;
import com.softpo.myteacyclopedia.R;
import com.softpo.myteacyclopedia.adapters.MyAdapter;
import com.softpo.myteacyclopedia.adapters.ViewPageAdapter;
import com.softpo.myteacyclopedia.entitys.HeadImg;
import com.softpo.myteacyclopedia.entitys.Tea;
import com.softpo.myteacyclopedia.urls.Urls;
import com.softpo.myteacyclopedia.utils.HttpUtils;
import com.softpo.myteacyclopedia.utils.JsonTools;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadFragment extends Fragment {
    private String path= Urls.HEADLINE_URL+Urls.HEADLINE_TYPE;
    private String pathImg=Urls.HEADERIMAGE_URL;
    private PullToRefreshListView mListView;
    private TextView mEmpty;
    private int currentPosition = 0;

    private List<Tea> data=new ArrayList<>();
    private BaseAdapter adapter;

    private List<ImageView> mImageViews=new ArrayList<>();
    private PagerAdapter viewAdapter;
    private LoadingLayoutProxy mLoadingLayoutProxy;

    //分页加载数据
    private int page=1;

    //头布局圆点跟着图片联动
    private Handler mHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:

                    if(this.hasMessages(1)){
                        this.removeMessages(1);
                    }
                    currentPosition++;
                    if(currentPosition>2){
                        currentPosition = 0;
                        mViewPager.setCurrentItem(currentPosition,false);
                        mPoints[index].setEnabled(true);
                        index = currentPosition;
                        mPoints[currentPosition].setEnabled(false);
                    }else {
                        mViewPager.setCurrentItem(currentPosition);
                        mPoints[index].setEnabled(true);
                        index = currentPosition;
                        mPoints[currentPosition].setEnabled(false);
                    }
                    mHeadTitle.setText(mHeadTitles[currentPosition]);

                    this.sendEmptyMessageDelayed(110,4000);
                    break;

            }
        }
    };
    private ViewPager mViewPager;
    private TextView mHeadTitle;
    private String[] mHeadTitles;
    private ImageView mPoint1;
    private ImageView mPoint2;
    private ImageView mPoint3;
    private ImageView backTopImg;
    private ListView listView;
    private String[] mPaths;
    private ImageView[] mPoints;


    public HeadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_head, container, false);

        //1、
        initView(ret);

        initHeadImgData();


        //短点击事件
        initListViewLisener();

        //初始化下拉刷新的布局
        initLoadingLayout();

        //长按删除
        initListViewLongLisener();

        //回到顶部
        initBackTop();


        return ret;
    }
    private int index = 0;


    //请求头布局数据
    private void initHeadImgData() {

        HttpUtils.getBytes(pathImg,new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        byte[] byteImg = (byte[]) msg.obj;
                        if (byteImg != null) {

                            HeadImg imgPaths= JSON.parseObject(new String(byteImg),HeadImg.class);

                            mPaths = new String[imgPaths.getData().size()];
                            mHeadTitles = new String[imgPaths.getData().size()];
                            //Log.d("flag", "----------->handleMessage:=====" +imgPaths.getData().size());
                            for (int i = 0; i < imgPaths.getData().size(); i++) {
                                mPaths[i]=imgPaths.getData().get(i).getImage_s();
                                mHeadTitles[i]=imgPaths.getData().get(i).getTitle();
                            }
                            initHeadData(mPaths);
                        }

                        break;
                }
            }
        });

    }

    //初始化下拉刷新
    private void initLoadingLayout() {
        mLoadingLayoutProxy= (LoadingLayoutProxy) mListView.getLoadingLayoutProxy();
        mLoadingLayoutProxy.setRefreshingLabel("正在刷新...");

    }

    //回到顶部
    private void initBackTop() {
        backTopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(0);
            }
        });

    }

    //长按删除
    private void initListViewLongLisener() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {

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


                                data.get(0).getData().remove(position-2);

                                TranslateAnimation trans = new TranslateAnimation(
                                        Animation.RELATIVE_TO_SELF,0
                                        ,Animation.RELATIVE_TO_SELF,0
                                        ,Animation.RELATIVE_TO_SELF,1
                                        ,Animation.RELATIVE_TO_SELF,0);
                                trans.setDuration(1000);
                                int count = listView.getChildCount();
                                int current = view.getTop();
                                for(int i=0;i<count;i++){
                                    View itemView = listView.getChildAt(i);
                                    if(itemView.getTop()>=current) {
                                        itemView.setAnimation(trans);
                                        trans.start();
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

    //点击跳转到详情
    private void initListViewLisener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private int index2 = 1;
    //3、获取头条数据
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    byte[] bytes= (byte[]) msg.obj;

                    if (bytes != null) {
                        Tea tea = JsonTools.loadString(new String(bytes));
                        data.add(tea);
//                        adapter.notifyDataSetChanged();


                        //刷新停止
                        if(mListView.isRefreshing()){
                            //暂停刷新
                            mListView.onRefreshComplete();

                            String time = DateUtils.formatDateTime(getContext(),
                                    System.currentTimeMillis(),
                                    DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
                            mLoadingLayoutProxy.setLastUpdatedLabel(time);
                        }

                        if(index2 == 1) {
                            index2++;
                            initListView(data);
                        }else{
                            adapter.notifyDataSetChanged();
                        }

                    }

                    break;
            }
        }
    };

    private void initHeadViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPoints[index].setEnabled(true);
                index = position;
                mPoints[position].setEnabled(false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    //设置适配器
    private void initListView(final List<Tea> data) {
        adapter = new MyAdapter(getContext(),data);

        mListView.setAdapter(adapter);
            listView.setEmptyView(mEmpty);

            View view=LayoutInflater.from(getContext()).inflate(R.layout.head_item,listView,false);
            mViewPager = (ViewPager) view.findViewById(R.id.vp);

            //初始化头布局控件
            initHead(view);


            viewAdapter = new ViewPageAdapter(mImageViews);


            AbsListView.LayoutParams layoutParams = new AbsListView
                    .LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            listView.addHeaderView(view);
            mViewPager.setAdapter(viewAdapter);
            mViewPager.setCurrentItem(currentPosition);
            mPoint1.setEnabled(false);

            if(mHeadTitles!=null)
                mHeadTitle.setText(mHeadTitles[0]);

            //轮播发送消息
            mHandler2.sendEmptyMessageDelayed(110,4000);

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

    //初始化头布局控件
    private void initHead(View view) {
        mHeadTitle = (TextView) view.findViewById(R.id.headTitle);
        mPoint1 = (ImageView) view.findViewById(R.id.point1);
        mPoint2 = (ImageView) view.findViewById(R.id.point2);
        mPoint3 = (ImageView) view.findViewById(R.id.point3);

        mPoints = new ImageView[]{mPoint1,mPoint2,mPoint3};

    }

    //头布局
    private void initHeadData(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            Log.d("flag", "----------->initListView:头布局路径" +paths[i]);

            //获取头布局图片
            HttpUtils.getBytes(paths[i],new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 0:
                            byte[] bitmapByte= (byte[]) msg.obj;

                            Log.d("flag", "----------->initListView:mybitMAP==="+bitmapByte.length);

                            if (bitmapByte != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
                                Log.d("flag", "----------->handleMessage:bitmap" +bitmap.toString());

                                ImageView imageView=new ImageView(getContext());
                                imageView.setImageBitmap(bitmap);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                                mImageViews.add(imageView);
                            }
                            initData(path+page);

                            break;
                    }
                }
            });

        }

    }


    //2、联网请求获取数据
    private void initData(String sPath) {
        HttpUtils.getBytes(sPath,mHandler);

    }

    //1、初始化控件
    private void initView(View ret) {
        mListView = (PullToRefreshListView) ret.findViewById(R.id.listView);

        listView = mListView.getRefreshableView();

        backTopImg = (ImageView) ret.findViewById(R.id.backTopImg);

        mEmpty = (TextView) ret.findViewById(R.id.empty);
    }

}
