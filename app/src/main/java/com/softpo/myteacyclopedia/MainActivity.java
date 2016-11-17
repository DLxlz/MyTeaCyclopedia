package com.softpo.myteacyclopedia;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softpo.myteacyclopedia.adapters.MyFragmentPagerAdapter;
import com.softpo.myteacyclopedia.fragments.HeadFragment;
import com.softpo.myteacyclopedia.fragments.ItemFragment;
import com.softpo.myteacyclopedia.sqlite.MySQLiteOpenHelper;
import com.softpo.myteacyclopedia.urls.Urls;
import com.softpo.viewpagertransformer.RotateUpTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] path={Urls.BASE_URL+Urls.CYCLOPEDIA_TYPE,
            Urls.BASE_URL+Urls.CONSULT_TYPE,
            Urls.BASE_URL+Urls.OPERATE_TYPE,
            Urls.BASE_URL+Urls.DATA_TYPE};

    private List<Fragment> mFragmentList=new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private TextView mCollect;
    private Button mSearch;
    private EditText mEdit;

    //数据库
    private MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private ImageView mBackHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new MySQLiteOpenHelper(this);
        db=dbHelper.getReadableDatabase();

        getSupportActionBar().hide();

        initView();

        initViewPager();

        initTabLayout();

        initDrawerListener();

        initMsearchListener();

        initMcollectListener();

        //设置返回home点击事件
        initBackHome();

    }

    //返回home点击事件
    private void initBackHome() {
        mBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

    }

    //我的收藏
    private void initMcollectListener() {
        mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CollectActivity.class);

                MainActivity.this.startActivity(intent);
            }
        });

    }

    //搜索
    private void initMsearchListener() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTrim = mEdit.getText().toString().trim();
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("search",searchTrim);

                MainActivity.this.startActivity(intent);
            }
        });

    }

    private void initDrawerListener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


    }


    private void initTabLayout() {

        //联动方法一
        mTabLayout.setupWithViewPager(mViewPager);
        //方法二
        //viewPager跟着tabLayout联动
//        mTabLayout.addOnTabSelectedListener(
 //               new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
//
//        //tabLayout跟着ViewPager联动
//        mViewPager.addOnPageChangeListener(
//                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    private void initViewPager() {
        String[] titles={"头条","百科","咨询","经营","数据"};

        HeadFragment headFragment=new HeadFragment();

        mFragmentList.add(headFragment);
        for (int i = 0; i <4; i++) {
            ItemFragment fragment=new ItemFragment();

            fragment.setPath(path[i]);
            mFragmentList.add(fragment);

        }

        FragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),mFragmentList,titles);

        mViewPager.setAdapter(adapter);

        //fragment切换动画
        mViewPager.setPageTransformer(true,new RotateUpTransformer());

    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        //搜索
        mEdit = (EditText) findViewById(R.id.drawer_et_search);
        mSearch = (Button) findViewById(R.id.drawer_btn_search);
        mBackHome = (ImageView) findViewById(R.id.drawer_back_home);
        //收藏夹
        mCollect = (TextView) findViewById(R.id.drawer_tv_mycollect);

    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.rightHome:
                if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.drawer_ib_back:
                if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }
                break;

        }


    }
}
