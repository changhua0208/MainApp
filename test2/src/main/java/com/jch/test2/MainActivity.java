package com.jch.test2;

import android.os.Bundle;

import com.jch.plugin.PluginActivity;

/**
 * @author changhua.jiang
 * @since 2018/7/9 下午7:08
 */

public class MainActivity extends PluginActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
