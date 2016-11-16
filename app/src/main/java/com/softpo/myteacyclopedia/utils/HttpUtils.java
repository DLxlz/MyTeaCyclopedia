package com.softpo.myteacyclopedia.utils;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by my on 2016/11/11.
 */
public class HttpUtils {
    public static void getBytes(final String path, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream is=null;
                ByteArrayOutputStream baos=null;
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);

                    if(conn.getResponseCode()==200){
                        is=conn.getInputStream();
                        baos=new ByteArrayOutputStream();

                        int len=0;
                        byte[] buf=new byte[1024*8];

                        while((len=is.read(buf))!=-1){
                            baos.write(buf,0,len);
                        }
                        if(baos.toByteArray().length==0)
                            return;
                        byte[] bytes = baos.toByteArray();

                        Message msg= Message.obtain();
                        msg.what=0;
                        msg.obj=bytes;
                        handler.sendMessage(msg);

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }
}
