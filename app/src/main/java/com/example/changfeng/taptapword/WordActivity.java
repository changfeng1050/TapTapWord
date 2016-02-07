package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WordActivity extends Activity implements View.OnClickListener {

    private static final String TAG ="WordActivity";

    public static final String EXTRA_WORD_NAME = "word_name";
    public static final String EXTRA_WORD_PHONE = "phone";
    public static final String EXTRA_WORD_MEANS = "means";
    public static final String EXTRA_WORD_ARCHIVED = "archived";

    @Bind(R.id.word_name)
    TextView wordNameEditText;
    @Bind(R.id.archive)
    CheckBox archiveCheckBox;
    @Bind(R.id.phone)
    TextView phoneEditText;
    @Bind(R.id.means)
    TextView meansEditText;
    @Bind(R.id.cancel)
    Button cancelButton;
    @Bind(R.id.save)
    Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        wordNameEditText.setText(intent.getStringExtra(EXTRA_WORD_NAME));
        wordNameEditText.setPadding(10, 20, 20, 10);
        wordNameEditText.setTextColor(Color.parseColor(MainActivity.SELECTED_COLOR));

        archiveCheckBox.setChecked(intent.getBooleanExtra(EXTRA_WORD_ARCHIVED, true));

        phoneEditText.setText(intent.getStringExtra(EXTRA_WORD_PHONE));
        phoneEditText.setPadding(30, 20, 20, 10);
        phoneEditText.setTextColor(Color.parseColor(MainActivity.WORD_TEXT_COLOR));

        meansEditText.setText(intent.getStringExtra(EXTRA_WORD_MEANS));
        meansEditText.setPadding(30, 20, 20, 10);
        meansEditText.setTextColor(Color.parseColor(MainActivity.WORD_TEXT_COLOR));

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.save:
                setResults();
                finish();
                break;
            default:
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word, menu);
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

    private void setResults() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WORD_NAME, wordNameEditText.getText().toString());
        intent.putExtra(EXTRA_WORD_PHONE, phoneEditText.getText().toString());
        intent.putExtra(EXTRA_WORD_MEANS, meansEditText.getText().toString());
        intent.putExtra(EXTRA_WORD_ARCHIVED, archiveCheckBox.isChecked());
        setResult(Activity.RESULT_OK, intent);
    }
}
