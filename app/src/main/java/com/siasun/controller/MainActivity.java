package com.siasun.controller;

import android.os.Bundle;
import android.widget.TextView;

import com.siasun.controller.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.hello_word)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView.setText("哈喽，世界,我来了");
    }
}
