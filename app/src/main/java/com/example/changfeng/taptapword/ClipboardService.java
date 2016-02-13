package com.example.changfeng.taptapword;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

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


    ClipboardManager.OnPrimaryClipChangedListener cbListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            if (cb.hasPrimaryClip()) {
                for (int i = 0; i < cb.getPrimaryClip().getItemCount(); i++) {

                    if (cb.getPrimaryClip().getItemAt(i).getText() != null && cb.getPrimaryClip().getItemAt(i).getText().length() > 0) {
                        if (!cb.getPrimaryClip().getItemAt(i).getText().toString().trim().isEmpty()) {
                            String clipboardItem = cb.getPrimaryClip().getItemAt(i).getText().toString().trim();

                            if (!Utils.isEnglishWord(clipboardItem)) {
                                break;
                            }

                            Intent intent = new Intent(getApplicationContext(), ConsultWordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(clipboardText, clipboardItem);
                            intent.putExtra(ConsultWordActivity.TYPE, ConsultWordActivity.TYPE_COPY);
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
