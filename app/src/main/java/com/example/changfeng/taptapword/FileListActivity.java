package com.example.changfeng.taptapword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by changfeng on 2015/3/4.
 */
public class FileListActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "FileListActivity";
    public static final String FILE_KEY_WORD = "word_ninja";

    List<FileInfo> fileInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_file_list);


        Intent intent = getIntent();
        fileInfoList = getFileList(intent.getStringExtra(FILE_KEY_WORD));
        if (fileInfoList.isEmpty()) {
            fileInfoList.add(new FileInfo(getString(R.string.filename_no_backup_file_found), getString(R.string.filepath_no_backup_file_found)));
        }

        final FileListAdapter adapter = new FileListAdapter(FileListActivity.this,
                R.layout.file_list_item, fileInfoList);

        ListView listView = (ListView) findViewById(R.id.file_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileInfo fileInfo = fileInfoList.get(position);
        if (!fileInfo.fileName.equals(getString(R.string.filename_no_backup_file_found))) {
            setResults(fileInfo);
        }
        finish();
    }

    private void setResults(FileInfo fileInfo) {
        Intent intent = new Intent();
        intent.putExtra(FILE_KEY_WORD, fileInfo.filePath);
        setResult(Activity.RESULT_OK, intent);
//        Log.d(TAG, "onItemClick() called filepath:" + fileInfo.filePath);
    }

    private ArrayList<FileInfo> getFileList(String fileKeyWord) {
        ArrayList<FileInfo> list = new ArrayList<>();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            File files[] = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    if (file.getName().contains(fileKeyWord)) {
                        list.add(new FileInfo(file.getName(), file.getPath()));
                    }
                }
            }
        }
        return list;
    }
}
