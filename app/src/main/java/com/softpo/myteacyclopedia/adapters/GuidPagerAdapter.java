package com.softpo.myteacyclopedia.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by my on 2016/11/15.
 */
public class GuidPagerAdapter extends PagerAdapter {
    List<ImageView> data;

    public GuidPagerAdapter(List<ImageView> data) {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View ret=data.get(position);
        container.addView(ret);

        return ret;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
 //       super.destroyItem(container, position, object);

        container.removeView(data.get(position));
    }
}
