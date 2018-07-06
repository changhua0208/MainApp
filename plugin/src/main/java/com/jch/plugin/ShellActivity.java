package com.jch.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import com.jch.plugin.axml.AXmlHolder;
import com.jch.plugin.hook.AMSHookHelper;
import com.jch.plugin.loader.ClassLoaderCreator;
import com.jch.plugin.model.PluginInfo;

/**
 * 宿主activity，具有发起插件能力
 * @author changhua.jiang
 * @since 2018/7/5 下午5:02
 */

public class ShellActivity extends Activity {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        AMSHookHelper.hookActivityManagerNative();
        AMSHookHelper.hookActivityThreadHandler();
    }

    //插件文件根路径
    public String getPluginRootPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void loadApp(final PluginInfo info){
        if(ClassLoaderCreator.isClassLoaderExists(info)) {
            startActivity(info);
        }
        else{
            onPluginLoading();
            new AsyncTask(){

                @Override
                protected Object doInBackground(Object[] objects) {
                    ClassLoaderCreator.create(ShellActivity.this,ShellActivity.this.getClassLoader(),info);
                    //android插件清单文件解析
                    AXmlHolder.init(info);
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    onPluginLoadFinished();
                    startActivity(info);
                }
            }
            .execute();
        }
    }

    public void startActivity(final PluginInfo info){
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("PLUGIN", info);
        startActivity(intent);
    }

    //第一次加载插件需要些时间,在这段时间内可以干点事情
    public void onPluginLoading(){

    }
    //第一次加载插件完成
    public void onPluginLoadFinished(){

    }

}
