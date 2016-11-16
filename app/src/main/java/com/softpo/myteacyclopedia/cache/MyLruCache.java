package com.softpo.myteacyclopedia.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by my on 2016/11/16.
 */

public class MyLruCache extends LruCache<String,Bitmap> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MyLruCache(int maxSize) {
        super(maxSize);
    }

    //LruCache链表
    //向LruCache存入数据(Bitmap),须告知其图片的大小
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight();
    }
}
