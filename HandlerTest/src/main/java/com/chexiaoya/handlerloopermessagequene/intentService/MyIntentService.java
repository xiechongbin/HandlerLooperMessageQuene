package com.chexiaoya.handlerloopermessagequene.intentService;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chexiaoya.handlerloopermessagequene.Value;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * IntentService 使工作线程逐一处理所有启动请求，如果你不需要在service中执行并发任务，intentService是最好的选择。
 * 实现onHandleIntent方法。
 * Created by xcb on 2018/8/6.
 * 通过IntentService 实现后台下载图片的功能
 */
public class MyIntentService extends IntentService {
    public static final String PIC_URL = "https://ws1.sinaimg.cn/large/610dc034ly1fgepc1lpvfj20u011i0wv.jpg";
     private static Handler uiHandler;//ui 线程的handler；

    public MyIntentService() {
        super(MyIntentService.class.getSimpleName());
    }

    public MyIntentService(String name) {
        super(name);
    }

    public static void setUiHandler(Handler handler) {
        uiHandler = handler;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        Log.d("test", url);
        try {
            Bitmap bitmap = downloadUrlToBitmap(url);
            if (bitmap != null && uiHandler != null) {
                Message message = uiHandler.obtainMessage();
                message.what = Value.MSG_VALUE_6;
                message.obj = bitmap;
                uiHandler.sendMessage(message);
                Log.d("test", "sendMessage");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("test", e.toString());
        }
    }

    /**
     * 下载图片
     *
     * @param url 图片地址
     * @return bitmap
     * @throws Exception 异常
     */
    private Bitmap downLoadPicture(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        BufferedInputStream stream = new BufferedInputStream(connection.getInputStream(), 8 * 1024);
        return BitmapFactory.decodeStream(stream);
    }

    private Bitmap downloadUrlToBitmap(String url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        urlConnection.disconnect();
        in.close();
        return bitmap;
    }
}
