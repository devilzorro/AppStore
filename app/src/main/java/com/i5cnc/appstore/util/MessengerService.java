package com.i5cnc.appstore.util;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

public class MessengerService extends Service {

    private static String TAG = "RepluginHostActivity";

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private class MessengerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.e(TAG, "handleMessage: 收到插件发来的消息："+msg.getData().toString());

                    //回复消息
                    Messenger client = msg.replyTo;
                    Bundle replyData = new Bundle();
                    replyData.putString("reply","插件发来的消息，宿主已收到");
                    Message replyMsg = Message.obtain();
                    replyMsg.what = 0;
                    replyMsg.setData(replyData);
                    try {
                        client.send(replyMsg);
                    } catch (Exception e) {
                        Log.e(TAG, "handleMessage: ",e);
                    }
                    break;
                case 2:
                    break;
                    default:
                        break;
            }
        }
    }
}
