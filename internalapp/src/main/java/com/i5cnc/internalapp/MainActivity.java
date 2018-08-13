package com.i5cnc.internalapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "PluginActivity";

    Button btnSend;
    EditText recvContent;
    EditText sendContent;

    Messenger mClientMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "插件收到消息: ");
            switch (msg.what) {
                case 0:
                    Bundle data = msg.getData();

                    Log.e(TAG, "Replugin Activity recv: " + data.getString("reply"));
                    recvContent.setText(data.getString("reply"));
                    break;
                case 1:
                    break;
                    default:
                        break;
            }
        }
    });

    private Messenger mServerMessenger;

    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mServerMessenger = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServerMessenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = findViewById(R.id.btn_send);
        sendContent = findViewById(R.id.edt_send);
        recvContent = findViewById(R.id.edt_recv);

        bindMessengerService();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RePlugin.getHostContext().unbindService(mMessengerConnection);
    }

    private void bindMessengerService() {
        Intent service = new Intent("com.i5cnc.appstore.util.MessengerService");
        service.setComponent(new ComponentName("com.i5cnc.appstore","com.i5cnc.appstore.util.MessengerService"));
        RePlugin.getHostContext().bindService(service,mMessengerConnection,Service.BIND_AUTO_CREATE);

        bindService(service,mMessengerConnection,BIND_AUTO_CREATE);
    }

    public void sendMsg() {
        String msgContent = sendContent.getText().toString();
        if (!"".equals(msgContent)) {
            Message message = Message.obtain();
            message.what = 1;
            Bundle data = new Bundle();
            data.putString("msg",msgContent);
            message.setData(data);
            message.replyTo = mClientMessenger;
            try {
                mServerMessenger.send(message);
            } catch (Exception e) {
                Log.e(TAG, "replugin activity sendMsg exception: ",e);
            }
        } else {
            Toast.makeText(MainActivity.this,"请输入发送内容！",Toast.LENGTH_LONG).show();
        }
    }
}
