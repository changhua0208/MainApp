package com.jch.plugin;

import android.app.Application;

import com.jch.plugin.hook.AMSHookHelper;

/**
 * @author changhua.jiang
 * @since 2018/1/31 下午5:55
 */

public class PluginTools {
    public static Application app;

    public static void init(Application app){
        init(app,true);
    }

    /**
     * 插件初始化
     * @param app application
     * @param hookable 是否支持直接启动activity，用了hook可以支持，不用hook只能支持activity代理方式
     */
    public static void init(Application app,boolean hookable){
        PluginTools.app = app;
        if(hookable)
            AMSHookHelper.init(app);
    }

    public static Application getApplication(){
        return app;
    }
}
