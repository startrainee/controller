package com.siasun.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.siasun.controller.fragment.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_main)
public class MainFragment extends BaseFragment {

    @ViewInject(R.id.text_view)
    private TextView textView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText("哈喽,时间");
    }

}
