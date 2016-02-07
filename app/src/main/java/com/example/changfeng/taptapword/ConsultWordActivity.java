package com.example.changfeng.taptapword;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;


public class ConsultWordActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ConsultWordActivity";

    private static final int MSG_WHAT_YOUDAO_DICT_RESULT = 1;

    private static final String NO_RESULT = "无法找到结果！";
    private static final int STATE_ORIGIN = 1;
    private EditText wordEditText;
    private int state;
    YoudaoResult youdaoResult;
    String query;
    Gson gson = new Gson();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_YOUDAO_DICT_RESULT:
                    resultTextView.setVisibility(View.VISIBLE);
                    if (state == STATE_ORIGIN) {
                        if (youdaoResult == null) {
                            resultTextView.setText(NO_RESULT);
                            return;
                        }
                        StringBuilder result = new StringBuilder();
                        result.append(query);
                        result.append("\n").append(youdaoResult.getDictResult());

                        resultTextView.setText(result.toString());
                        saveWord();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_consult_word);

        ImageButton addButton = (ImageButton) findViewById(R.id.add_new_word_button);
        addButton.setOnClickListener(this);
        wordEditText = (EditText) findViewById(R.id.word_edit_text);

        resultTextView = (TextView) findViewById(R.id.result_text_view);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_word_button:
                resultTextView.setText("");
                resultTextView.setVisibility(View.GONE);
                String word = wordEditText.getText().toString();
                if (isEnglishWord(word)) {
                    state = STATE_ORIGIN;
                    sendRequestWithHttpClient(word, "en", "zh");
                } else {
                    showToast(getResources().getString(R.string.msg_not_support_other_language));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consult_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendRequestWithHttpClient(final String query, final String from, final String to) {
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
//                if (youdaoResult != null && !youdaoResult.getParsedExplains().equals(query) && !youdaoResult.getParseWebTranslate().equals(query)) {
                Message message = new Message();
                message.what = MSG_WHAT_YOUDAO_DICT_RESULT;
                handler.sendMessage(message);
//                }
            }
        }).start();
    }

    void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }


    private Boolean isEnglishWord(String word) {
        char[] array = word.toCharArray();
        for (char a : array) {
            if ((char) (byte) a != a) {
                return false;
            }
        }
        return true;
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
}
