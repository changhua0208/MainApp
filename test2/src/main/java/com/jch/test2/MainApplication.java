package com.jch.test2;

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
        //子程序自己启动时，完全没有必要启动hook就能达到效果
        PluginTools.init(this,false);
    }
}