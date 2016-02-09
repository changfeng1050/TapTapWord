package com.example.changfeng.taptapword;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.view.MaterialListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by changfeng on 2015/4/17.
 */
public class AboutFragment extends Fragment {

    private static final String TAG = "AboutFragment";

    @Bind(R.id.material_list_view)
    MaterialListView materialListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.material_list_view, container, false);
        ButterKnife.bind(this, view);

        WelcomeCard card1 = new WelcomeCard(getActivity());

        card1.setTitle("单词忍者");
        card1.setTitleColor(getResources().getColor(R.color.colorGreen));
        card1.setDescription("Spy into words for you!\nversion " + getVersion() + "\n\nPowered by changfeng\nEmail:changfeng1050@hotmail.com");
        card1.setDescriptionColor(getResources().getColor(R.color.colorGrey));
        materialListView.add(card1);

        return view;
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageInfo pi = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            return getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
