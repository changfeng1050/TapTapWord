package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WordActivity extends Activity {

    private static final String TAG = "WordActivity";

    public static final String EXTRA_WORD_NAME = "word_name";

    @Bind(R.id.word_name)
    TextView wordNameEditText;

    @Bind(R.id.date_time)
    TextView dateTimeTextView;

    @Bind(R.id.archive)
    CheckBox archiveCheckBox;

    @Bind(R.id.phone_label)
    TextView phoneLabelTextView;

    @Bind(R.id.phone)
    TextView phoneTextView;

    @Bind(R.id.means_label)
    TextView meansLabelTextView;
    @Bind(R.id.means)
    TextView meansTextView;

    @Bind(R.id.web_explains_label)
    TextView webExpainsLabelTextView;
    @Bind(R.id.web_explains)
    TextView webExplainsTextView;

    @Bind(R.id.note_label)
    TextView noteLabelTextView;
    @Bind(R.id.note)
    EditText noteEditText;

    @OnClick(R.id.cancel)
    void quit(View v) {
        finish();
    }

    @OnClick(R.id.save)
    void save(View v) {
        updateWord();
        WordManger.get(this).updateWord(word);
        showToast(getString(R.string.message_save_success));
        finish();
    }

    Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String wordName = intent.getStringExtra(EXTRA_WORD_NAME);
        word = WordManger.get(this).getWord(wordName);
        if (word != null) {
            wordNameEditText.setText(word.getName());

            dateTimeTextView.setText(String.format("%04d-%02d-%02d %02d:%02d:%02d", word.getYear(), word.getMonth(), word.getDate(), word.getHour(), word.getMinute(), word.getMinute()));

            archiveCheckBox.setChecked(word.isArchived());

            phoneTextView.setText(word.getFormatPhones());

            meansTextView.setText(word.getMeans());

            webExplainsTextView.setText(word.getWebExplains());

            noteEditText.setText(word.getNote());

            if (word.getFormatPhones().isEmpty()) {
                phoneLabelTextView.setVisibility(View.GONE);
                phoneTextView.setVisibility(View.GONE);
            }

            if (word.getMeans() == null || word.getMeans().isEmpty()) {
                meansLabelTextView.setVisibility(View.GONE);
                meansTextView.setVisibility(View.GONE);
            }

            if (word.getWebExplains() == null || word.getWebExplains().isEmpty()) {
                webExpainsLabelTextView.setVisibility(View.GONE);
                webExplainsTextView.setVisibility(View.GONE);
            }
        }
    }


    private void updateWord() {
        boolean archived = archiveCheckBox.isChecked();
        word.setArchived(archived);
        word.setMeans(meansTextView.getText().toString());
        word.setNote(noteEditText.getText().toString());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
