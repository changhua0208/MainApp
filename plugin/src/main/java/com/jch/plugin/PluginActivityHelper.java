package com.jch.plugin;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jch.plugin.model.PluginInfo;

/**
 * @author changhua.jiang
 * @since 2017/9/25 下午3:09
 */

public class PluginActivityHelper {

    public static void startActivity(Context context, PluginInfo info, Class<? extends PluginActivity> cl, Bundle bundle) {
        if (info != null) {
            Intent intent = new Intent(context, ProxyActivity.class);
            PluginInfo info1 = new PluginInfo();
            info1.setPackageName(cl.getPackage().getName());
            info1.setParent(context.getClass().getName());
            info1.setApkName(info.getApkName());
            info1.setApkPath(info.getApkPath());
            info1.setClassName(cl.getSimpleName());
            intent.putExtra("PLUGIN", info1);
            if (bundle != null)
                intent.putExtra("Bundle", bundle);
            context.startActivity(intent);
        } else {
            //如果没有插件，说明只是子程序运行
            try {
                Intent intent = new Intent(context, ProxyActivity.class);
                intent.putExtra("LocalActivityClass", cl.getName());
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void startActivityForResult(Context context, PluginInfo info, Class<? extends PluginActivity> cl, int requestCode, Bundle bundle) {
        if (info != null) {
            Intent intent = new Intent(context, ProxyActivity.class);
            PluginInfo info1 = new PluginInfo();
            info1.setPackageName(cl.getPackage().getName());
            info1.setParent(context.getClass().getName());
            info1.setApkName(info.getApkName());
            info1.setApkPath(info.getApkPath());
            info1.setClassName(cl.getSimpleName());
            intent.putExtra("PLUGIN", info1);
            if (bundle != null)
                intent.putExtra("Bundle", bundle);
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            //如果没有插件，说明只是子程序运行
            try {
                Intent intent = new Intent(context, ProxyActivity.class);
                intent.putExtra("LocalActivityClass", cl.getName());
                ((Activity)context).startActivityForResult(intent,requestCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static View findViewById(Context context, int resId) {
        View view = ((Activity) context).findViewById(resId);
        return view;
    }

    public static void finish(Context context) {
        ((Activity) context).finish();
    }

    public static LayoutInflater getLayoutInflater(Context context) {
        return ((Activity)context).getLayoutInflater();//LayoutInflater.from(context);
    }

    public static Application getApplication(Context context) {
        return ((Activity) context).getApplication();
    }

    public static boolean onTouchEvent(Context context, MotionEvent event) {
        return ((Activity) context).onTouchEvent(event);
    }

    public static Object getSystemService(Context context, String name) {
        return context.getSystemService(name);
    }

    public static ComponentName startService(Context context, Intent intent) {
        return context.startService(intent);
    }

    public static boolean bindService(Context context, Intent intent, ServiceConnection conn, int flags) {
        return context.bindService(intent, conn, flags);
    }

    public static boolean stopService(Context context, Intent intent) {
        return context.stopService(intent);
    }

    public static void unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    public static Intent getIntent(Context context) {
        return ((Activity) context).getIntent();
    }

    public static void setResult(Context context, int code, Intent intent) {
        ((Activity) context).setResult(code, intent);
    }

    public static ActionBar getActionBar(Context context) {
        return ((Activity) context).getActionBar();
    }

    public static Window getWindow(Context context) {
        return ((Activity) context).getWindow();
    }

    public static WindowManager getWindowManager(Context context) {
        return ((Activity) context).getWindowManager();
    }

    public static void startActivity(PluginActivity pluginActivity, Intent intent) throws ClassNotFoundException{
        startActivityForResult(pluginActivity,intent,-1);
    }

    public static void startActivityForResult(PluginActivity pluginActivity, Intent intent,int requestCode) throws ClassNotFoundException{
//        Activity context = pluginActivity.getCurrentActivity();
//        ComponentName component = intent.getComponent();
//        PluginInfo info = pluginActivity.getPluginInfo();
//        if (component != null) {
//            Class clazz = pluginActivity.getClass().getClassLoader().loadClass(component.getClassName());
//            if (clazz.isAssignableFrom(PluginActivity.class)) {
//                //启动activity代理模式
//                startActivityForResult(context,info,clazz,requestCode,intent.getExtras());
//            }
//            else{
//                //启动真滴activity
//                if(info != null){
//                    info = info.clone();
//                    info.setParent(pluginActivity.getClass().getName());
//                    info.setPackageName(clazz.getPackage().getName());
//                    info.setClassName(clazz.getSimpleName());
//                    intent.putExtra("PLUGIN",info);
//                }
//                context.startActivityForResult(intent,requestCode);
//            }
//
//        } else {
//            context.startActivityForResult(intent,requestCode);
//        }
        pluginActivity.getCurrentActivity().startActivityForResult(intent,requestCode);
    }

    public static Resources getResources(Context mContext) {
        return mContext.getResources();
    }

    public static AssetManager getAssets(Context mContext){
        return mContext.getAssets();
    }

    public static void setTitle(Context mContext, String title) {
        ((Activity)mContext).setTitle(title);
    }
}


