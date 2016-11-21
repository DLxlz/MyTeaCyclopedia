package com.softpo.myteacyclopedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softpo.myteacyclopedia.R;
import com.softpo.myteacyclopedia.cache.MyLruCache;
import com.softpo.myteacyclopedia.entitys.Tea;

import java.util.List;

/**
 * Created by my on 2016/11/12.
 */
public class MyAdapter extends BaseAdapter{
    private Context context;
    private List<Tea> data;
    //定义内存缓存
    private MyLruCache myLruCache;


    public MyAdapter(Context context, List<Tea> data) {
        this.context=context;
        this.data=data;
        //给数据分配八分之一内存空间
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        myLruCache=new MyLruCache(maxSize);
    }

    @Override
    public int getCount() {
          return data!=null?(data.size()!=0?data.get(0).getData().size():0):0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(0).getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret=null;
        ViewHolder holder=null;
        if(convertView!=null){
            ret=convertView;
            holder= (ViewHolder) ret.getTag();
        }else {
            ret= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            holder=new ViewHolder();

            holder.title= (TextView) ret.findViewById(R.id.showTitle);
            holder.source= (TextView) ret.findViewById(R.id.showSource);
            holder.nickName= (TextView) ret.findViewById(R.id.showNickname);
            holder.creatTime= (TextView) ret.findViewById(R.id.showCreatTime);
            holder.thumb = (ImageView) ret.findViewById(R.id.showThumb);

            ret.setTag(holder);
        }

        Tea.DataBean dataBean = data.get(0).getData().get(position);
        //赋值
        holder.title.setText(dataBean.getTitle());
        holder.source.setText(dataBean.getSource());
        holder.nickName.setText(dataBean.getNickname());
        holder.creatTime.setText(dataBean.getCreate_time());

        if(!dataBean.getWap_thumb().equals("")){
            holder.thumb.setVisibility(View.VISIBLE);
//
//            final String path=dataBean.getWap_thumb();
//            Bitmap bitmap=Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_4444);
//            holder.thumb.setImageBitmap(bitmap);
//
//            //从缓存中获取数据
//            Bitmap cacheBitmap = getCache(path);
//
//            if(cacheBitmap!=null){
//                holder.thumb.setImageBitmap(cacheBitmap);
//            }else {//第三级网络获取数据
//                holder.thumb.setTag(dataBean.getWap_thumb());
//                getImage(path,holder.thumb);
//            }

            Glide
                    .with(context)
                    .load(dataBean.getWap_thumb())
                    .into(holder.thumb);


        }else {
            holder.thumb.setVisibility(View.GONE);
        }

        return ret;
    }

    //从网络获取缓存
//    private void getImage(final String path, final ImageView imageView){
//
//        HttpUtils.getBytes(path,new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 0:
//                        String tag = (String) imageView.getTag();
//                        if(tag.equals(path)){
//                            byte[] bytes= (byte[]) msg.obj;
//                            Bitmap imgBitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                            imageView.setImageBitmap(imgBitmap);
//
//                            //将数据存储到内存中
//                            myLruCache.put(path.replaceAll("/",""),imgBitmap);
//
//                            //将数据存入磁盘中
//                            String root=context.getExternalCacheDir().getAbsolutePath();
//                            SdCardUtil.saveFile(bytes,root,path.replaceAll("/",""));
//
//                        }
//
//                }
//            }
//        },context);
//
//
//    }
//
//    //从缓存中获取数据
//    private Bitmap getCache(String img){
//        img=img.replaceAll("/","");
//        Bitmap bitmap = myLruCache.get(img);
//        if(bitmap!=null){
//            return bitmap;
//        }else {//内存中没有从磁盘中取数据
//            String root=context.getExternalCacheDir().getAbsolutePath();
//            String fileName=root+ File.separator+img;
//            byte[] bytes= SdCardUtil.pickFromSdCard(fileName);
//            if(bytes!=null){
//                Bitmap bitmapSd=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                //将磁盘中的数据保存到内存中
//                myLruCache.put(img,bitmapSd);
//                return bitmapSd;
//            }
//        }
//
//        return null;
//    }


    private static class ViewHolder{
        TextView title,source,creatTime,nickName;
        ImageView thumb;

    }
}
