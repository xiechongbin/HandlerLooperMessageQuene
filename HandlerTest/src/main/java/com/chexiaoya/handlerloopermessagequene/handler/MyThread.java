package com.chexiaoya.handlerloopermessagequene.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.chexiaoya.handlerloopermessagequene.MyThreadB;
import com.chexiaoya.handlerloopermessagequene.Value;

/**
 * 工作线程 子线程中默认是没有Looper的 如果在子线程中没有创建Looper的话 那么当消息插入的是在主线程的Looper中维护的MessageQuene
 * 那样的话 消息就存在同一个队列当中。子线程和主线程处理消息将会按照插入的先后顺序执行。
 * Created by xcb on 2018/8/1.
 */
public class MyThread extends Thread {
    private Handler handler;
    private Handler workHandler;

    public MyThread(Handler handler) {
        this.handler = handler;
    }

    public Handler getWorkHandler() {
        return workHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        workHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Value.MSG_VALUE_2) {
                    String threadName = Thread.currentThread().getName();
                    String main = Looper.getMainLooper().getThread().getName();
                    Log.d("test", "Looper = " + Looper.myLooper().getThread() + ">>主线程" + main + "给子线程" + threadName + "发来消息：收到" + msg.arg1);
                    Message message = handler.obtainMessage();
                    message.arg1 = 123;
                    message.what = Value.MSG_VALUE_3;
                    MyThreadB.getWorkHandlerB().sendMessage(message);
                }
            }
        };
        if (handler != null) {
            for (int i = 0; i < 5; i++) {
                Message message = handler.obtainMessage();
                message.what = Value.MSG_VALUE_1;
                message.arg1 = i;
                handler.sendMessage(message);
            }

        }
        Looper.loop();
    }

    public void onClose() {
        //workHandler.removeCallbacksAndMessages(null);
    }
}
