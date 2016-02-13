package com.example.changfeng.taptapword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";
    static final int REQUEST_BACKUP_FILE = 1;
    private static final String FILE_KEY_WORD = "word_ninja";



    @Bind(R.id.youdao_dict_switch)
    Switch youdaoDictSwitch;
    @Bind(R.id.web_explain_switch)
    Switch webExplainSwitch;
    @Bind(R.id.youdao_translate_switch)
    Switch youdaoTranslateSwitch;
    @Bind(R.id.baidu_translate_switch)
    Switch baiduTranslateSwitch;

    @Bind(R.id.backup_data)
    TextView backupDataTextView;
    @Bind(R.id.restore_data)
    TextView restoreDataTextView;
    @Bind(R.id.clear_backup_data)
    TextView clearBackupDataTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        backupDataTextView.setOnClickListener(this);
        restoreDataTextView.setOnClickListener(this);
        clearBackupDataTextView.setOnClickListener(this);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backup_data:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.backup)
                        .setMessage(R.string.message_backup_data)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (WordManger.get(getActivity()).copyDbToSdcard()) {
                                    showToast(R.string.message_data_backupped);
                                } else {
                                    showToast(R.string.message_data_backupped_failed);
                                }
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                break;
            case R.id.restore_data:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.restore)
                        .setMessage(R.string.message_restore_data)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), FileListActivity.class);
                                intent.putExtra(FileListActivity.FILE_KEY_WORD, "word_ninja");
                                startActivityForResult(intent, REQUEST_BACKUP_FILE);
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                break;
            case R.id.clear_backup_data:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.clear_backup_data)
                        .setMessage(R.string.message_clear_backup_data)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearAllBackupData();
                                showToast(getString(R.string.message_all_backup_data_cleared), Toast.LENGTH_SHORT);
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult() called resultCode :" + requestCode + " requestCode :" + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_BACKUP_FILE:
                    if (WordManger.get(getActivity()).restoreDb(data.getExtras().getString(FileListActivity.FILE_KEY_WORD))) {
                        showToast(getString(R.string.message_data_restored));
                    } else {
                        showToast(getString(R.string.message_data_restored_failed));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void clearAllBackupData() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            File files[] = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    if (file.getName().contains(FILE_KEY_WORD)) {
                        MyFile.deleleFile(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private void readSettings(){
        SharedPreferences pref  = getActivity().getSharedPreferences(SharedPref.PREFERENCE_NAME, Context.MODE_PRIVATE);
        youdaoDictSwitch.setChecked(pref.getBoolean(SharedPref.PREFERENCE_YOUDAO_DICT, true));
        webExplainSwitch.setChecked(pref.getBoolean(SharedPref.PREFERENCE_WEB_EXPLAIN, true));
        youdaoTranslateSwitch.setChecked(pref.getBoolean(SharedPref.PREFERENCE_YOUDAO_TRANSLATE, true));
        baiduTranslateSwitch.setChecked(pref.getBoolean(SharedPref.PREFERENCE_DAIDU_TRANSLATE, true));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(SharedPref.PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(SharedPref.PREFERENCE_YOUDAO_DICT, youdaoDictSwitch.isChecked())
                .putBoolean(SharedPref.PREFERENCE_WEB_EXPLAIN, webExplainSwitch.isChecked())
                .putBoolean(SharedPref.PREFERENCE_YOUDAO_TRANSLATE, youdaoTranslateSwitch.isChecked())
                .putBoolean(SharedPref.PREFERENCE_DAIDU_TRANSLATE, baiduTranslateSwitch.isChecked())
                .apply();
    }



    private void showToast(int resourceId) {
        Toast.makeText(getActivity(), getString(resourceId), Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message, int duration) {
        Toast.makeText(getActivity(), message, duration).show();
    }
}