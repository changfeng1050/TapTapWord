package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;


public class ResultActivity extends Activity {

    private static final String TAG = "ResultActivity";
    private static final int MSG_WHAT_YOUDAO_DICT_RESULT = 1;
    private static String result = "";
    private static final String NO_RESULT = "无法找到结果！";
    private String clip;
    private int state;
    private static final int STATE_ORIGIN = 1;
    private static final int STATE_LOWERCASE = 2;

    private TextView translateResultTextView;

    private String word_name;
    private String ph_en;
    private String ph_am;
    private StringBuilder word_means;

    private String query;

    TranslateHelper translateHelper = new TranslateHelper();
    BaiduResult baiduResult;
    YoudaoResult youdaoResult;
    Gson gson = new Gson();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        translateResultTextView = (TextView) findViewById(R.id.translate_result);

        clip = getIntent().getStringExtra(ClipboardService.clipboardText);

        result = "";
        state = STATE_ORIGIN;
        sendRequestWithHttpClient(clip, "en", "zh");
        sharedPreferences = getSharedPreferences(SettingsFragment.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_YOUDAO_DICT_RESULT:
                    if (state == STATE_ORIGIN) {
                        translateResultTextView.setTextColor(Color.parseColor(MainActivity.SELECTED_COLOR));
                        if (youdaoResult == null && baiduResult == null) {
                            translateResultTextView.setText("没有找到结果");
                            return;
                        }
                        StringBuilder result = new StringBuilder();
                        result.append(query);
                        try {
                            if (youdaoResult != null && youdaoResult.getUkPhonetic() != null && youdaoResult.getUsPhonetic() != null
                                    && !youdaoResult.getUkPhonetic().isEmpty()
                                    && !youdaoResult.getUsPhonetic().isEmpty()) {
                                result.append("\n\nUK:[").append(youdaoResult.getUkPhonetic()).append("]").append(" US:[").append(youdaoResult.getUsPhonetic()).append("]");
                            }
                        } catch (Exception e) {

                        }
                        try {
                            if (sharedPreferences.getBoolean(SettingsFragment.SHARED_PREFERENCE_YOUDAO_DICT, false)) {
                                if (youdaoResult != null && youdaoResult.getParsedExplains() != null && !youdaoResult.getParsedExplains().isEmpty()) {
                                    result.append("\n\n").append(youdaoResult.getParsedExplains());
                                }
                            }
                        } catch (Exception e){

                        }

                        try {
                            if (sharedPreferences.getBoolean(SettingsFragment.SHARED_PREFERENCE_WEB_EXPLAIN, false)) {
                                if (youdaoResult != null && youdaoResult.getWeb() != null && ! youdaoResult.getWeb().isEmpty()) {
                                    result.append("\n\n").append(youdaoResult.getWeb());
                                }
                            }
                        } catch (Exception e){

                        }

                        try {
                            if (sharedPreferences.getBoolean(SettingsFragment.SHARED_PREFERENCE_YOUDAO_TRANSLATE, false)) {
                                if (youdaoResult != null && youdaoResult.getTranslateResult() != null && !youdaoResult.getTranslateResult().isEmpty()) {
                                    result.append("\n\n").append(youdaoResult.getTranslateResult());
                                }
                            }
                        } catch (Exception e){

                        }

                        try {
                            if (sharedPreferences.getBoolean(SettingsFragment.SHARED_PREFERENCE_YOUDAO_TRANSLATE, false)) {
                                if (baiduResult != null && baiduResult.getResult() != null && !baiduResult.getResult().isEmpty()) {
                                    result.append("\n\n").append(baiduResult.getResult());
                                }
                            }
                        } catch (Exception e) {

                        }

                        translateResultTextView.setText(result.toString());
                        saveWord();

                    }
                    break;
                default:
                    break;
            }
        }
    };



    private void sendRequestWithHttpClient(final String query, final String from, final String to) {
        this.query = query;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    youdaoResult = gson.fromJson(translateHelper.getYoudaoResult(query), YoudaoResult.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    youdaoResult = null;
                }

                try {
                    baiduResult = gson.fromJson(translateHelper.getBaiduResult(query), BaiduResult.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    baiduResult = null;
                }

                Message message = new Message();
                message.what = MSG_WHAT_YOUDAO_DICT_RESULT;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void saveWord() {
        Word word = new Word();
        word.setLanguage("English");
        word.setName(query);
        word.setEnPhone(youdaoResult.getUkPhonetic());
        word.setAmPhone(youdaoResult.getUsPhonetic());
        try {
            word.setMeans(youdaoResult.getParsedExplains());
        } catch (NullPointerException expected) {
            return;
        }
        word.setArchived(false);
        Calendar c = Calendar.getInstance();
        word.setYear(c.get(Calendar.YEAR));
        word.setMonth(c.get(Calendar.MONTH) + 1);
        word.setDate(c.get(Calendar.DAY_OF_MONTH));
        word.setHour(c.get(Calendar.HOUR_OF_DAY));
        word.setMinute(c.get(Calendar.MINUTE));
        word.setSecond(c.get(Calendar.SECOND));

        WordManger.get(getApplicationContext()).insertWord(word);
    }



    void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

}
