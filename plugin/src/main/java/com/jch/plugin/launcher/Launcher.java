package com.jch.plugin.launcher;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.jch.plugin.PluginActivityCreator;
import com.jch.plugin.ProxyActivity;

/**
 * 用于子工程项目的发起
 * @author changhua.jiang
 * @since 2018/1/10 上午11:43
 */

public class Launcher extends ProxyActivity {
    public void onCreate(Bundle savedStatusInstance){
        super.onCreate(savedStatusInstance);
        ActivityInfo info= null;
        try {
            info = this.getPackageManager()
                    .getActivityInfo(getComponentName(),
                            PackageManager.GET_META_DATA);
            String launcherName =info.metaData.getString("Launcher");
            mPluginActivity = PluginActivityCreator.create(this,launcherName);
            mPluginActivity.onCreate(savedStatusInstance);
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
