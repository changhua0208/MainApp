package com.jch.plugin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;


/**
 * 使用activty代理的情况下，发起的是该activity
 * @author changhua.jiang
 * @since 2017/9/22 下午5:24
 */

public class ProxyActivity extends BaseActivity {
    protected PluginActivity mPluginActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPluginInfo != null) {
            mPluginActivity = PluginActivityCreator.create(this, mPluginInfo);
        } else {
            Intent in = getIntent();
            String clName = in.getStringExtra("LocalActivityClass");
            if(clName != null)
                mPluginActivity = PluginActivityCreator.create(this, clName);
        }

        if (mPluginActivity != null) {
            mPluginActivity.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        if (mPluginActivity != null)
            mPluginActivity.onConfigurationChanged(config);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mPluginActivity != null)
            mPluginActivity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPluginActivity != null)
            mPluginActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPluginActivity != null)
            mPluginActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPluginActivity != null)
            mPluginActivity.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPluginActivity != null)
            mPluginActivity.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPluginActivity != null)
            mPluginActivity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);
        if (mPluginActivity != null)
            ret = mPluginActivity.onTouchEvent(event);
        return ret;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mPluginActivity != null)
            mPluginActivity.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        if (mPluginActivity != null)
            ret = mPluginActivity.onCreateOptionsMenu(menu);
        return ret;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean ret = super.onKeyDown(keyCode, event);
        if (mPluginActivity != null)
            ret = mPluginActivity.onKeyDown(keyCode, event);
        return ret;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        boolean ret = super.onKeyLongPress(keyCode, event);
        if (mPluginActivity != null)
            ret = mPluginActivity.onKeyLongPress(keyCode, event);
        return ret;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean ret = super.onKeyUp(keyCode, event);
        if (mPluginActivity != null)
            ret = mPluginActivity.onKeyUp(keyCode, event);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = super.onOptionsItemSelected(item);
        if (mPluginActivity != null)
            ret = mPluginActivity.onOptionsItemSelected(item);
        return ret;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(mPluginActivity != null)
            mPluginActivity.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onAttachedToWindow() {
        if(mPluginActivity != null)
            mPluginActivity.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        if(mPluginActivity != null)
            mPluginActivity.onDetachedFromWindow();
    }

//    public PluginInfo getPluginInfo() {
//        if (mPluginActivity == null) {
//            return null;
//        } else {
//            return mPluginActivity.getPluginInfo();
//        }
//    }

}
