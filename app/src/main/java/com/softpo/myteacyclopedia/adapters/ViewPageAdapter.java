package com.softpo.myteacyclopedia.adapters;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by my on 2016/11/13.
 */
public class ViewPageAdapter extends PagerAdapter {
    private List<ImageView> imageViews;

    public ViewPageAdapter(List<ImageView> imageViews) {
        this.imageViews=imageViews;
    }

    @Override
    public int getCount() {
//        return imageViews!=null?imageViews.size():0;
 //       Log.d("flag", "----------->getCount:" +imageViews);
        if(imageViews.size()==0){
            return 0;
        }
        return imageViews!=null?imageViews.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("flag", "----------->instantiateItem:");
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    //    super.destroyItem(container, position, object);

        Log.d("flag", "----------->destroyItem:");
        container.removeView(imageViews.get(position));

    }
}
