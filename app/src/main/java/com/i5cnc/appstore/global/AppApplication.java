package com.i5cnc.appstore.global;

import com.lzy.okgo.OkGo;
import com.qihoo360.replugin.RePluginApplication;

public class AppApplication extends RePluginApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
