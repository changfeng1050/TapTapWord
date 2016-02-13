package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;


public class ConsultWordActivity extends Activity {

    private static final String TAG = "ConsultWordActivity";

    public static final int MSG_WHAT_WORD_RESULT = 1;
    public static final String TYPE = "type";
    public static final int TYPE_CONSULT = 1;
    public static final int TYPE_COPY = 2;
    private int type = TYPE_CONSULT;

    BaiduResult baiduResult;
    YoudaoResult youdaoResult;
    String query;
    Gson gson = new Gson();

    boolean responsed = false;

    @Bind(R.id.word_consult_layout)
    LinearLayout wordConsultLayout;

    @Bind(R.id.word_edit_text)
    EditText wordEditText;


    @OnClick(R.id.add_new_word_text_view)
    void addNewWord(View view) {
        resetViews();
        String word = wordEditText.getText().toString();
        resultInfoTextView.setVisibility(View.VISIBLE);
        if (Utils.isEnglishWord(word)) {
            resultInfoTextView.setText(getString(R.string.requesting_result_from_internet));
            sendRequestWithHttpClient(word, "en", "zh");
        } else {
            resultInfoTextView.setText(getString(R.string.message_not_support_other_language));
        }
    }

    @Bind(R.id.result_info_text_view)
    TextView resultInfoTextView;

    @Bind(R.id.word_result_layout)
    LinearLayout wordResultLayout;
    @Bind(R.id.word_name_text_view)
    TextView wordNameTextView;
    @Bind(R.id.word_phones_text_view)
    TextView wordPhonesTextView;
    @Bind(R.id.word_means_text_view)
    TextView wordMeansTextView;
    @Bind(R.id.word_web_explains_text_view)
    TextView wordWebExplainsTextView;
    @Bind(R.id.youdao_translate_text_view)
    TextView youdaoTranslateTextView;
    @Bind(R.id.baidu_translate_text_view)
    TextView baiduTranslateTextView;
    @Bind(R.id.add_note_text_view)
    TextView addNoteTextView;

    @OnClick(R.id.add_note_text_view)
    void addNote(View view) {
        addNoteTextView.setVisibility(View.GONE);
        noteEditText.setVisibility(View.VISIBLE);
    }

    @Bind(R.id.note_edit_text)
    EditText noteEditText;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_WORD_RESULT:
                    responsed = true;
                    if (youdaoResult == null && baiduResult == null) {
                        resultInfoTextView.setText(getString(R.string.message_cannot_find_word_result));
                        return;
                    }
                    resultInfoTextView.setText("");

                    wordNameTextView.setText(query);
                    try {
                        wordPhonesTextView.setText(youdaoResult.getFormatPhones());
                    } catch (Exception e) {

                    }
                    SharedPreferences pref = getSharedPreferences(SharedPref.PREFERENCE_NAME, MODE_PRIVATE);
                    if (pref.getBoolean(SharedPref.PREFERENCE_YOUDAO_DICT, true)) {
                        try {
                            wordMeansTextView.setText(youdaoResult.getParsedExplains());
                        } catch (Exception e) {

                        }
                    }

                    if (pref.getBoolean(SharedPref.PREFERENCE_WEB_EXPLAIN, true)) {
                        try {
                            wordWebExplainsTextView.setText(youdaoResult.getParseWebTranslate());
                        } catch (Exception e) {

                        }
                    }

                    if (pref.getBoolean(SharedPref.PREFERENCE_YOUDAO_TRANSLATE, true)) {
                        try {
                            youdaoTranslateTextView.setText(youdaoResult.getTranslateResult());
                        } catch (Exception e) {

                        }
                    }

                    if (pref.getBoolean(SharedPref.PREFERENCE_DAIDU_TRANSLATE, true)) {
                        try {
                            baiduTranslateTextView.setText(baiduResult.getResult());
                        } catch (Exception e) {

                        }
                    }
                    updateViewVisibility();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_consult_word);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra(TYPE, TYPE_CONSULT);
        if (type == TYPE_COPY) {
            wordConsultLayout.setVisibility(View.GONE);
            resultInfoTextView.setVisibility(View.VISIBLE);
            resultInfoTextView.setText(getString(R.string.requesting_result_from_internet));
            String clip = getIntent().getStringExtra(ClipboardService.clipboardText).trim();
            sendRequestWithHttpClient(clip, "en", "zh");
        }

        updateViewVisibility();
    }

    @Override
    protected void onDestroy() {
        if (responsed) {
            saveWord();
        }
        super.onDestroy();
    }

    private void sendRequestWithHttpClient(final String query, final String from, final String to) {
        responsed = false;
        this.query = query;
        new Thread(new Runnable() {
            @Override
            public void run() {

                TranslateHelper translateHelper = new TranslateHelper();

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
                message.what = MSG_WHAT_WORD_RESULT;
                handler.sendMessage(message);
            }
        }).start();
    }

    void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }


    private void saveWord() {
        if (query == null || query.isEmpty()) {
            return;
        }
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
        word.setWebExplains(youdaoResult.getParseWebTranslate());
        word.setArchived(false);
        word.setNote(noteEditText.getText().toString());

        Calendar c = Calendar.getInstance();
        word.setYear(c.get(Calendar.YEAR));
        word.setMonth(c.get(Calendar.MONTH) + 1);
        word.setDate(c.get(Calendar.DAY_OF_MONTH));
        word.setHour(c.get(Calendar.HOUR_OF_DAY));
        word.setMinute(c.get(Calendar.MINUTE));
        word.setSecond(c.get(Calendar.SECOND));

        WordManger.get(getApplicationContext()).insertWord(word);
    }

    private void updateViewVisibility() {
        boolean wordNameEmpty = wordNameTextView.getText().toString().isEmpty();
        boolean wordPhonesEmpty = wordPhonesTextView.getText().toString().isEmpty();
        boolean wordMeansEmpty = wordMeansTextView.getText().toString().isEmpty();
        boolean wordWebExplainsEmpty = wordWebExplainsTextView.getText().toString().isEmpty();
        boolean youdaoTranslateEmpty = youdaoTranslateTextView.getText().toString().isEmpty();
        boolean baiduTranslateEmpty = baiduTranslateTextView.getText().toString().isEmpty();
        boolean resultInfoEmpty = resultInfoTextView.getText().toString().isEmpty();

        if (wordNameEmpty) {
            wordNameTextView.setVisibility(View.GONE);
        } else {
            wordNameTextView.setVisibility(View.VISIBLE);
        }

        if (wordPhonesEmpty) {
            wordPhonesTextView.setVisibility(View.GONE);
        } else {
            wordPhonesTextView.setVisibility(View.VISIBLE);
        }

        if (wordMeansEmpty) {
            wordMeansTextView.setVisibility(View.GONE);
        } else {
            wordMeansTextView.setVisibility(View.VISIBLE);
        }

        if (wordWebExplainsEmpty) {
            wordWebExplainsTextView.setVisibility(View.GONE);
        } else {
            wordWebExplainsTextView.setVisibility(View.VISIBLE);
        }

        if (youdaoTranslateEmpty) {
            youdaoTranslateTextView.setVisibility(View.GONE);
        } else {
            youdaoTranslateTextView.setVisibility(View.VISIBLE);
        }

        if (baiduTranslateEmpty) {
            baiduTranslateTextView.setVisibility(View.GONE);
        } else {
            baiduTranslateTextView.setVisibility(View.VISIBLE);
        }

        if (wordPhonesEmpty && wordMeansEmpty && wordWebExplainsEmpty && youdaoTranslateEmpty && baiduTranslateEmpty) {
            addNoteTextView.setVisibility(View.GONE);
        } else {
            addNoteTextView.setVisibility(View.VISIBLE);
        }

        if (resultInfoEmpty) {
            resultInfoTextView.setVisibility(View.GONE);
        } else {
            resultInfoTextView.setVisibility(View.VISIBLE);
        }


    }

    private void resetViews() {
        wordNameTextView.setText("");
        wordPhonesTextView.setText("");
        wordMeansTextView.setText("");
        wordWebExplainsTextView.setText("");
        youdaoTranslateTextView.setText("");
        baiduTranslateTextView.setText("");
        noteEditText.setText("");
        resultInfoTextView.setText("");
        updateViewVisibility();
    }
}
