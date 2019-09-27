package com.chexiaoya.handlerloopermessagequene.handlerThread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.chexiaoya.handlerloopermessagequene.Value;

/**
 * HandlerThread handler是Android 提供的一个已经为我们创建好Looper的线程类
 * Created by xcb on 2018/8/1.
 */
public class MyHandlerThread extends HandlerThread implements Handler.Callback {

    private Handler workHandler;//工作线程预与之相对应的handler
    private Handler mainHandler;//主线程的handler

    /**
     * 构造方法
     *
     * @param name 线程名称
     */
    public MyHandlerThread(String name, Handler mainHandler) {
        super(name);
        this.mainHandler = mainHandler;
    }

    /**
     * 构造方法
     *
     * @param name     线程名称
     * @param priority 优先级
     */
    public MyHandlerThread(String name, int priority) {
        super(name, priority);
    }


    /**
     * 获取工作线程的handler
     *
     * @return workHandler
     */
    public Handler getWorkHandler() {
        return workHandler;
    }

    /**
     * Looper 准备完成回调
     */
    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        workHandler = new Handler(this.getLooper(), this);
//        for (int i = 0; i < 5; i++) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Message message = workHandler.obtainMessage();
//            message.what = Value.MSG_VALUE_4;
//            message.arg1 = i;
//            mainHandler.sendMessage(message);
//        }
    }

    /**
     * 安全退出
     */
    @Override
    public boolean quitSafely() {
        return super.quitSafely();
    }

    /**
     * 消息处理回调方法
     */
    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == Value.MSG_VALUE_3) {
            Log.d("test", "主线程：" + Looper.getMainLooper().getThread().getName() + " 给HandlerThread线程发来消息：" + msg.arg1);
        } else if (msg.what == Value.MSG_VALUE_8) {
            Log.d("test", "主线程：" + Looper.getMainLooper().getThread().getName() + " 给HandlerThread线程发来消息：");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                    Log.d("test", "sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
