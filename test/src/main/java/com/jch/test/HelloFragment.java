package com.jch.test;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author changhua.jiang
 * @since 2018/2/6 下午4:24
 */

public class HelloFragment extends Fragment {
    TextView tvHello;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tvHello.setText("hello fragment!");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_fourth,null);
        tvHello = (TextView) view.findViewById(R.id.tv_hello_fmt);
        new Handler().postDelayed(runnable,3000);
        return view;
    }
}
