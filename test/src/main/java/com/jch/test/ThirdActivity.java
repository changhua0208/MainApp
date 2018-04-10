package com.jch.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jch.plugin.PluginActivity;

/**
 * @author changhua.jiang
 * @since 2018/1/31 下午5:18
 */

public class ThirdActivity extends PluginActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_thr);
        findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getCurrentActivity(),SecandActivity.class);
        startActivity(intent);
    }
}
