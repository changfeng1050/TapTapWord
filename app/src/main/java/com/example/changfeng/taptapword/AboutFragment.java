package com.example.changfeng.taptapword;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by changfeng on 2015/4/17.
 */
public class AboutFragment extends Fragment {

    private static final String TAG = "AboutFragment";

    @Bind(R.id.version_text_view)
    TextView versionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        if (!getVersion().isEmpty()) {
            versionTextView.setText(getVersion());
        }

        return view;
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            return getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
