package com.example.changfeng.taptapword;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClipboardService extends Service {

    private static final String TAG = "ClipboardService";

    public static final String clipboardText = "clipboardText";

    private ClipboardManager cb;

    public ClipboardService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setPrimaryClip(ClipData.newPlainText("", ""));
        cb.addPrimaryClipChangedListener(cbListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cb.removePrimaryClipChangedListener(cbListener);
        MyLog.d(TAG, "onDestroy()");
    }

    private Boolean isEnglishWord(String word) {


       // 判断是否有汉字
        char[] array = word.toCharArray();
        for (char a : array) {
            if ((char) (byte) a != a) {
                return false;
            }
        }

        // 判断是否有数字
        Pattern digitPattern = Pattern.compile("[0-9]{2,}");
        if (digitPattern.matcher(word).find()) {
            return false;
        }
        // 判断是是否是网址
        Pattern httpPattern = Pattern.compile("(http://|https://){1}[//w//.//-/:]+");
        if (httpPattern.matcher(word).find()) {
            return false;
        }

        // 判断是否不包含特殊字符
        Pattern specialPattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
        return !specialPattern.matcher(word).matches();
    }



   ClipboardManager.OnPrimaryClipChangedListener cbListener = new ClipboardManager.OnPrimaryClipChangedListener() {
       @Override
       public void onPrimaryClipChanged() {
           if (cb.hasPrimaryClip()) {
               for (int i = 0; i < cb.getPrimaryClip().getItemCount(); i++) {

                   if (cb.getPrimaryClip().getItemAt(i).getText() != null && cb.getPrimaryClip().getItemAt(i).getText().length() > 0) {
                       if (!cb.getPrimaryClip().getItemAt(i).getText().toString().trim().isEmpty()) {
                           String clipboardItem = cb.getPrimaryClip().getItemAt(i).getText().toString().trim();

                           if (!isEnglishWord(clipboardItem)) {
                               break;
                           }



                           Intent intent = new Intent();
                           intent.setClassName("com.example.changfeng.taptapword", "com.example.changfeng.taptapword.ResultActivity");
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           intent.putExtra(clipboardText, clipboardItem);
                           startActivity(intent);
                           break;
                       }
                   }
               }
           }
       }
   };

    void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
