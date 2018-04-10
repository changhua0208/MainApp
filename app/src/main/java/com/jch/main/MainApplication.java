package com.jch.main;

import android.app.Application;

import com.jch.plugin.PluginTools;

/**
 * @author changhua.jiang
 * @since 2017/9/22 下午6:38
 */
@Deprecated
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PluginTools.init(this);
    }
}
