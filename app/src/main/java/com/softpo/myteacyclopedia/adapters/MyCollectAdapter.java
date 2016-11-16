package com.softpo.myteacyclopedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softpo.myteacyclopedia.R;
import com.softpo.myteacyclopedia.entitys.Tea;

import java.util.List;

/**
 * Created by my on 2016/11/15.
 */
public class MyCollectAdapter extends BaseAdapter {
    private Context context;
    private List<Tea.DataBean> data;

    public MyCollectAdapter(Context context, List<Tea.DataBean> data) {
        this.context=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            ret= LayoutInflater.from(context).inflate(R.layout.collect_list_item,parent,false);
            holder=new ViewHolder();

            holder.title= (TextView) ret.findViewById(R.id.showTitle);
            holder.source= (TextView) ret.findViewById(R.id.showSource);
            holder.nickname= (TextView) ret.findViewById(R.id.showNickname);
            holder.createTime= (TextView) ret.findViewById(R.id.showCreatTime);

            ret.setTag(holder);
        }

        Tea.DataBean dataBean = data.get(position);
        //赋值
        holder.title.setText(dataBean.getTitle());
        holder.source.setText(dataBean.getSource());
        holder.nickname.setText(dataBean.getNickname());
        holder.createTime.setText(dataBean.getCreate_time());


        return ret;
    }

    private static class ViewHolder{
        TextView title,source,createTime,nickname;
    }
}
