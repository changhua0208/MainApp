package com.jch.plugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.jch.plugin.axml.AXmlHolder;
import com.jch.plugin.clazz.ReflectUtils;
import com.jch.plugin.loader.ClassLoaderCreator;
import com.jch.plugin.model.PluginInfo;

/**
 * @author changhua.jiang
 * @since 2018/1/31 上午11:59
 */

public class BaseActivity extends Activity {
    protected PluginInfo mPluginInfo;
    private Resources.Theme mThemeNew;
    private Resources mResource;
    private AssetManager mAssetManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        mPluginInfo = in.getParcelableExtra("PLUGIN");
        if (mPluginInfo != null) {
            replaceClassLoader();
            mAssetManager = createAssetManager();
            mResource = createResourceInner();
            mThemeNew = createThemeInner();
            //replaceResource();
            //replaceTheme();
        }
    }


    protected AssetManager createAssetManager() {
        AssetManager assetManager = null;
        if (mPluginInfo != null) {
            try {
                assetManager = AssetManager.class.newInstance();
                ReflectUtils.invokeMethod(assetManager, "addAssetPath", mPluginInfo.getApkUri());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("createAssetManager error");
            }
        } else {
            assetManager = super.getAssets();
        }
        return assetManager;
    }

    protected Resources createResourceInner() {
        Resources resources = null;
        if (mPluginInfo != null) {
            Resources superRes = super.getResources();
            resources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
        } else {
            resources = super.getResources();
        }
        return resources;
    }

    public Resources.Theme createThemeInner() {
        Resources.Theme theme = null;
        if (mPluginInfo != null) {
            Resources.Theme superTheme = super.getTheme();
            ActivityInfo mActivityInfo = (ActivityInfo) ReflectUtils.getFieldValue(Activity.class, this, "mActivityInfo");
            if (mActivityInfo != null) {
                if (mActivityInfo.theme > 0) {
                    setTheme(mActivityInfo.theme);
                    theme = getResources().newTheme();
                    theme.setTo(superTheme);
                    theme.applyStyle(mActivityInfo.theme, true);
                } else {
                    PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageArchiveInfo(mPluginInfo.getApkUri(), PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
                    int defaultTheme = packageInfo.applicationInfo.theme;
                    if (defaultTheme <= 0) {
                        if (Build.VERSION.SDK_INT >= 14) {
                            mActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                        } else {
                            mActivityInfo.theme = android.R.style.Theme;
                        }
                    } else {
                        mActivityInfo.theme = defaultTheme;
                    }
                    setTheme(mActivityInfo.theme);
                    theme = mResource.newTheme();
                    theme.setTo(superTheme);
                    theme.applyStyle(mActivityInfo.theme, true);
                }
            }
        } else {
            theme = super.getTheme();
        }
        return theme;
    }

    //直接暴力替
    private void replaceClassLoader(){

        Context baseContext = getBaseContext();
        ClassLoader cl = ClassLoaderCreator.create(this, super.getClassLoader(), mPluginInfo);
        Object loadedApk = ReflectUtils.getFieldValue("android.app.ContextImpl", baseContext, "mPackageInfo");
        ReflectUtils.setFieldValue("android.app.LoadedApk", loadedApk, "mClassLoader", cl);
        if(Build.VERSION.SDK_INT >= 26) {
            //Object loadedApk = ReflectUtils.getFieldValue("android.app.ContextImpl", baseContext, "mPackageInfo");
            ReflectUtils.setFieldValue("android.app.ContextImpl",baseContext,"mClassLoader",cl);
        }
    }

    private void replaceResource(){
        //android.view.ContextThemeWrapper
        ReflectUtils.setFieldValue("android.view.ContextThemeWrapper",this,"mResources",mResource);
    }

    private void replaceTheme(){
        ReflectUtils.setFieldValue("android.view.ContextThemeWrapper",this,"mTheme", mThemeNew);
    }

    @Override
    public Resources getResources() {
        if(mResource == null) {
            return super.getResources();
        }
        else{
            return mResource;
        }
    }

    @Override
    public Resources.Theme getTheme() {
        if(mThemeNew == null) {
            return super.getTheme();
        }
        else{
            return mThemeNew;
        }
    }

    @Override
    public AssetManager getAssets() {
        if(mAssetManager != null) {
            return super.getAssets();
        }
        else{
            return mAssetManager;
        }
    }

    //是否是独立运行还是镶嵌在宿主程序中的？
    private boolean isIndependent(){
        return mPluginInfo == null;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode,Bundle options) {
        ComponentName component = intent.getComponent();
        //显式调用
        if(component != null){
            try {
                Class clazz = getClassLoader().loadClass(component.getClassName());
                if(PluginActivity.class.isAssignableFrom(clazz)){
                    String packageName = PluginTools.getApplication().getPackageName();
                    ComponentName componentName = new ComponentName(packageName,ProxyActivity.class.getName());
                    intent.setComponent(componentName);
                    //如果是独立运行的话，启动的其实的ProxyActivity，并告诉ProxyActivity,PluginActivity在哪个
                    if(isIndependent())
                        intent.putExtra("LocalActivityClass",clazz.getName());
                }
                if(mPluginInfo != null) {
                    PluginInfo info = mPluginInfo.clone();
                    info.setClassName(clazz.getSimpleName());
                    info.setPackageName(clazz.getPackage().getName());
                    info.setParent(getClass().getName());
                    intent.putExtra("PLUGIN", info);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            //若不是独立运行，隐式调用需要单独处理
            if(!isIndependent()) {
                if (AXmlHolder.resolveIntent(mPluginInfo, intent)) {
                    if (mPluginInfo != null) {
                        PluginInfo info = mPluginInfo.clone();
                        info.setParent(getClass().getName());
                        ComponentName componentName = intent.getComponent();
                        String clazz = componentName.getClassName();
                        int index = clazz.lastIndexOf(".");

                        if (index > 0) {
                            String packageName = clazz.substring(0, index);
                            String clazzName = clazz.substring(index + 1);
                            info.setClassName(clazzName);
                            info.setPackageName(packageName);
                        }
                        intent.putExtra("PLUGIN", info);
                    }
                }
            }
        }
        super.startActivityForResult(intent,requestCode,options);
    }

}
