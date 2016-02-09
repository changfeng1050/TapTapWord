package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResultActivity extends Activity {

    private static final String TAG = "ResultActivity";
    private static final int MSG_WHAT_YOUDAO_DICT_RESULT = 1;
    private static String result = "";
    private static final String NO_RESULT = "无法找到结果！";
    private String clip;
    private int state;
    private static final int STATE_ORIGIN = 1;
    private static final int STATE_LOWERCASE = 2;

//    private TextView translateResultTextView;

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

    @Bind(R.id.translate_result)
    TextView translateResultTextView;
    @Bind(R.id.add_note)
    TextView addNoteTextView;
    @Bind(R.id.note_edit_text)
    EditText noteEditText;

    @OnClick(R.id.add_note)
    void showNoteEditText(View v) {
        if (noteEditText != null) {
            noteEditText.setVisibility(View.VISIBLE);
        }
        addNoteTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        clip = getIntent().getStringExtra(ClipboardService.clipboardText);

        result = "";
        state = STATE_ORIGIN;
        sendRequestWithHttpClient(clip, "en", "zh");
        sharedPreferences = getSharedPreferences(SharedPref.PREFERENCE_NAME, MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        saveWord();
        super.onDestroy();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_YOUDAO_DICT_RESULT:
                    if (state == STATE_ORIGIN) {
                        translateResultTextView.setTextColor(getResources().getColor(R.color.colorGreen));
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
                            if (sharedPreferences.getBoolean(SharedPref.PREFERENCE_YOUDAO_DICT, true)) {
                                if (youdaoResult != null && youdaoResult.getParsedExplains() != null && !youdaoResult.getParsedExplains().isEmpty()) {
                                    result.append("\n\n").append(youdaoResult.getParsedExplains());
                                }
                            }
                        } catch (Exception e) {

                        }

                        try {
                            if (sharedPreferences.getBoolean(SharedPref.PREFERENCE_WEB_EXPLAIN, true)) {
                                if (youdaoResult != null && youdaoResult.getWeb() != null && !youdaoResult.getWeb().isEmpty()) {
                                    result.append("\n\n").append(youdaoResult.getParseWebTranslate());
                                }
                            }
                        } catch (Exception e) {

                        }

                        try {
                            if (sharedPreferences.getBoolean(SharedPref.PREFERENCE_YOUDAO_TRANSLATE, true)) {
                                if (youdaoResult != null && youdaoResult.getTranslateResult() != null && !youdaoResult.getTranslateResult().isEmpty()) {
                                    result.append("\n\n").append(youdaoResult.getTranslateResult());
                                }
                            }
                        } catch (Exception e) {

                        }

                        try {
                            if (sharedPreferences.getBoolean(SharedPref.PREFERENCE_YOUDAO_TRANSLATE, true)) {
                                if (baiduResult != null && baiduResult.getResult() != null && !baiduResult.getResult().isEmpty()) {
                                    result.append("\n\n").append(baiduResult.getResult());
                                }
                            }
                        } catch (Exception e) {

                        }

                        translateResultTextView.setText(result.toString());
                        if (!result.toString().isEmpty()) {
                            addNoteTextView.setVisibility(View.VISIBLE);
                            addNoteTextView.setTextColor(getResources().getColor(R.color.colorGreen));
                        }
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
        word.setNote(noteEditText.getText().toString());
        word.setWebExplains(youdaoResult.getParseWebTranslate());

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
