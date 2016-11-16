package com.softpo.myteacyclopedia.utils;


import com.softpo.myteacyclopedia.entitys.Tea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/11/12.
 */

public class JsonTools {
    public static Tea loadString(String json){
        Tea tea=new Tea();
        try {
            JSONObject teaJo=new JSONObject(json);

            tea.setErrorMessage(teaJo.optString("errorMessage"));

            JSONArray dataJa = teaJo.optJSONArray("data");
            List<Tea.DataBean> data=new ArrayList<>();
            for (int i = 0; i < dataJa.length(); i++) {
                JSONObject dataJo = dataJa.optJSONObject(i);
                Tea.DataBean dataBean=new Tea.DataBean();

                dataBean.setCreate_time(dataJo.optString("create_time"));
                dataBean.setDescription(dataJo.optString("description"));
                dataBean.setId(dataJo.optString("id"));
                dataBean.setNickname(dataJo.optString("nickname"));
                dataBean.setSource(dataJo.optString("source"));
                dataBean.setTitle(dataJo.optString("title"));
                dataBean.setWap_thumb(dataJo.optString("wap_thumb"));

                data.add(dataBean);

            }
            tea.setData(data);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tea;
    }
}
