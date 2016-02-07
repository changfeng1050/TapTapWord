package com.example.changfeng.taptapword;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.view.MaterialListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by changfeng on 2015/5/4.
 */
public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";

    @Bind(R.id.material_list_view)
    MaterialListView materialListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.material_list_view, container, false);
        ButterKnife.bind(this, view);
//
        WelcomeCard welcomeCard = new WelcomeCard(getActivity());
        welcomeCard.setTitle(getString(R.string.title_welcome));
        welcomeCard.setTitleColor(Color.parseColor(MainActivity.SELECTED_COLOR));
        welcomeCard.setDescription(R.string.description_welcome);
        materialListView.add(welcomeCard);

        SmallImageCard titleOne = new SmallImageCard(getActivity());
        titleOne.setTitle(getString(R.string.title_tutorOne));
        titleOne.setTitleColor(Color.parseColor(MainActivity.SELECTED_COLOR));
        materialListView.add(titleOne);

        BigImageCard tutorOne = new BigImageCard(getActivity());
        tutorOne.setDrawable(R.drawable.open_reading_app);
        materialListView.add(tutorOne);

        SmallImageCard titleTwo = new SmallImageCard(getActivity());
        titleTwo.setTitle(getString(R.string.title_tutorTwo));
        titleTwo.setTitleColor(Color.parseColor(MainActivity.SELECTED_COLOR));
        materialListView.add(titleTwo);

        BigImageCard tutorTwo = new BigImageCard(getActivity());
        tutorTwo.setDrawable(R.drawable.select_word);
        materialListView.add(tutorTwo);

        SmallImageCard titleThree = new SmallImageCard(getActivity());
        titleThree.setTitle(getString(R.string.title_tutorThree));
        titleThree.setTitleColor(Color.parseColor(MainActivity.SELECTED_COLOR));
        materialListView.add(titleThree);

        BigImageCard tutorThree = new BigImageCard(getActivity());
        tutorThree.setDrawable(R.drawable.copy_word);
        materialListView.add(tutorThree);

        SmallImageCard titleFour = new SmallImageCard(getActivity());
        titleFour.setTitle(getString(R.string.title_tutorFour));
        titleFour.setTitleColor(Color.parseColor(MainActivity.SELECTED_COLOR));
        materialListView.add(titleFour);

        BigImageCard tutorFour = new BigImageCard(getActivity());
        tutorFour.setDrawable(R.drawable.result_word);
        materialListView.add(tutorFour);

        SmallImageCard titleFive = new SmallImageCard(getActivity());
        titleFive.setTitle(getString(R.string.title_tutorFive));
        titleFive.setTitleColor(Color.parseColor(MainActivity.SELECTED_COLOR));
        materialListView.add(titleFive);

        BigImageCard tutorFive = new BigImageCard(getActivity());
        tutorFive.setDrawable(R.drawable.refer_word);
        materialListView.add(tutorFive);

        return view;
    }
}
