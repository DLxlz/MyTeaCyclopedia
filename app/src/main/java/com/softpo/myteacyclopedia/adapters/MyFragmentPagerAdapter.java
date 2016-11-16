package com.softpo.myteacyclopedia.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by my on 2016/11/11.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private String[] titles;

    public MyFragmentPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> fragmentList, String[] titles) {
        super(supportFragmentManager);
        this.fragmentList=fragmentList;
        this.titles=titles;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList!=null?fragmentList.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    //    super.destroyItem(container, position, object);
    }
}
