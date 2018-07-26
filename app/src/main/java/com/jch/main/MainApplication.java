package com.jch.main;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.jch.plugin.ShellApplication;

/**
 * @author changhua.jiang
 * @since 2017/9/22 下午6:38
 */
public class MainApplication extends ShellApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
