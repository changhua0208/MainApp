package com.jch.plugin;

import android.app.Application;

/**
 * 宿主application
 * @author changhua.jiang
 * @since 2018/7/5 下午5:00
 */

public class ShellApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        PluginTools.init(this);
    }
}
