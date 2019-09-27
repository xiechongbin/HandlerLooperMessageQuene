package com.chexiaoya.handlerloopermessagequene;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chexiaoya.handlerloopermessagequene.handler.MyThread;
import com.chexiaoya.handlerloopermessagequene.handlerThread.MyHandlerThread;
import com.chexiaoya.handlerloopermessagequene.intentService.MyIntentService;

import java.lang.ref.WeakReference;

/**
 * 子线程给主线程相互发消息示例
 */
public class ThreadToMainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyHandler myHandler;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private ImageView imageView;
    private TextView tv_thread_to_main_tips;
    private MyThread myThread;
    private MyThreadB myThreadB;
    private MyHandlerThread myHandlerThread;
    private HandlerThread handlerThread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_to_main);
        button2 = (Button) findViewById(R.id.btn_2);
        button2.setOnClickListener(this);
        button3 = (Button) findViewById(R.id.btn_3);
        button3.setOnClickListener(this);
        button4 = (Button) findViewById(R.id.btn_4);
        button4.setOnClickListener(this);
        button5 = (Button) findViewById(R.id.btn_5);
        button5.setOnClickListener(this);
        button6 = (Button) findViewById(R.id.btn_6);
        button6.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.iv_pic);
        this.tv_thread_to_main_tips = (TextView) findViewById(R.id.tv_thread_to_main_tips);
        myHandler = new MyHandler(this);
        myThread = new MyThread(myHandler);
        myThreadB = new MyThreadB();
        myHandlerThread = new MyHandlerThread(MyHandlerThread.class.getSimpleName(), myHandler);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_2:
                myThread.start();
                myThreadB.start();
                break;
            case R.id.btn_3:
                handlerThread = new HandlerThread("test");
                handlerThread.start();
                handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        try {
                            if (msg.what == 111) {
                                Log.d("test", "handlerThread: 当前线程名称：" + Thread.currentThread().getName());
                            }
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        myHandler.sendEmptyMessage(Value.MSG_VALUE_5);
                        return false;
                    }
                });
                handler.sendEmptyMessage(111);//发送两次
                handler.sendEmptyMessage(111);
                break;
            case R.id.btn_4:
                myHandlerThread.start();
                finish();
                break;
            case R.id.btn_5:
                MyIntentService.setUiHandler(myHandler);
                Intent intent = new Intent(this, MyIntentService.class);
                intent.putExtra("url", MyIntentService.PIC_URL);
                startService(intent);
                break;
            case R.id.btn_6:
                MyHandlerThread handlerThread = new MyHandlerThread("MyHandlerThread", myHandler);
                handlerThread.start();
                Handler handler = new Handler(handlerThread.getLooper(),handlerThread);
                handler.sendEmptyMessageDelayed(Value.MSG_VALUE_8, 5000);
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (myThread != null) {
            myThread.onClose();
        }
        if (myThreadB != null) {
            myThreadB.onClose();
        }
    }

    /**
     * 通过继承并且软引用的方式包裹数据 防止oomF
     * Created by xcb on 2018/7/31.
     */
    private static class MyHandler extends BaseHandler {
        private WeakReference<ThreadToMainActivity> weakReference;
        private ThreadToMainActivity threadToMainActivity;

        private MyHandler(ThreadToMainActivity threadToMainActivity) {
            super(threadToMainActivity);
            weakReference = new WeakReference<>(threadToMainActivity);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null) {
                threadToMainActivity = weakReference.get();
            }
            String mainThreadName = threadToMainActivity.getMainLooper().getThread().getName();
            if (msg.what == Value.MSG_VALUE_1) {
                String threadName = threadToMainActivity.myThread.getName();
                threadToMainActivity.tv_thread_to_main_tips.setText("子线程：" + threadName + "给主线程" + mainThreadName + "发来消息：" + msg.arg1);
                Log.d("test", "子线程：" + threadName + "给主线程" + mainThreadName + "发来消息：" + msg.arg1);
                Handler workHandler = threadToMainActivity.myThread.getWorkHandler();

                Message message1 = workHandler.obtainMessage();
                message1.what = Value.MSG_VALUE_2;
                message1.arg1 = msg.arg1;
                workHandler.sendMessage(message1);

            } else if (msg.what == Value.MSG_VALUE_4) {
                Log.d("test", "子线程:HandlerThread" + "给主线程" + mainThreadName + "发来消息：" + msg.arg1);
                Message message = Message.obtain(threadToMainActivity.myHandlerThread.getWorkHandler());
                message.what = Value.MSG_VALUE_3;
                threadToMainActivity.myHandlerThread.getWorkHandler().sendMessage(message);
            } else if (msg.what == Value.MSG_VALUE_5) {
                Log.d("test", "子线程:HandlerThread" + "给主线程" + mainThreadName + "发来空消息：" + msg.arg1);
            } else if (msg.what == Value.MSG_VALUE_6) {
                Log.d("test", "子线程:下载图片服务" + "给主线程" + mainThreadName + "发来消息：下载成功");
                if (msg.obj instanceof Bitmap) {
                    threadToMainActivity.imageView.setImageBitmap((Bitmap) msg.obj);
                }

            }
        }
    }
}
