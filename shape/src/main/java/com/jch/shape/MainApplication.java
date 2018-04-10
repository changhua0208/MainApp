package com.jch.shape;

import android.app.Application;

import com.jch.plugin.PluginTools;

/**
 * @author changhua.jiang
 * @since 2018/1/31 下午5:59
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PluginTools.init(this);
    }
}
