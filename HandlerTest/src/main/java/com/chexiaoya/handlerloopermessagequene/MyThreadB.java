package com.chexiaoya.handlerloopermessagequene;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * 工作线程B
 * Created by xcb on 2018/8/1.
 */
public class MyThreadB extends Thread implements Handler.Callback {
    private static Handler workHandlerB;

    public static Handler getWorkHandlerB() {
        return workHandlerB;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        workHandlerB = new Handler(Looper.myLooper(), this);
        Looper.loop();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == Value.MSG_VALUE_3) {
            Log.d("test", "Looper = " + Looper.myLooper().getThread() + ">>子线程A给子线程B发来消息：收到" + msg.arg1);
        }
        return true;
    }

    public void onClose() {
        workHandlerB.removeCallbacksAndMessages(null);
    }
}
