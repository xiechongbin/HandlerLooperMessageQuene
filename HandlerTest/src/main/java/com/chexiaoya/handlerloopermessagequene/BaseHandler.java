package com.chexiaoya.handlerloopermessagequene;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * 自定义handler基类
 * Created by xcb on 2018/7/31.
 */
public class BaseHandler<T> extends Handler {
    public WeakReference<T> weakReference;

    public BaseHandler(T obj) {
        weakReference = new WeakReference<T>(obj);
    }

    public BaseHandler(Looper looper, T obj) {
        super(looper);
        weakReference = new WeakReference<T>(obj);
    }
}
