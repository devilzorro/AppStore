package com.i5cnc.appstore.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    private ToastUtil() {
    }

    public static void showToast(Context context, String text) {
        //没有则创建 有则改变内容
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}