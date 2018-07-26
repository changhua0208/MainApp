package com.jch.plugin;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jch.plugin.model.PluginInfo;

/**
 * 启动一个新的activity<br>
 * <code>
 *     Intent intent = new Intent(getCurrentActivity(),otherActivity.class);<br>
 *     startActivity(intent);
 * </code>
 * <br>otherActivity可以是{@link PluginActivity}的子类，也可以是{@link BaseActivity}的子类，Plugin会自动识别。
 *
 * @author changhua.jiang
 * @since 2017/9/22 下午5:44
 */

public abstract class PluginActivity {

    private Context mContext;
    private PluginInfo info;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public PluginInfo getPluginInfo() {
        return info;
    }

    public void setPluginInfo(PluginInfo info) {
        this.info = info;
    }

    public void onCreate(Bundle savedInstanceState){
    }

    public void onStart(){

    }

    public void onResume(){

    }

    public void onPause(){

    }

    public void onStop(){

    }

    public void onDestroy(){

    }

    public void onRestart() {

    }

    public void onConfigurationChanged(Configuration config) {
    }

    public Resources getResources(){
        return mContext.getResources();
    }

    public AssetManager getAssets(){
        return mContext.getAssets();
    }

    public void setContentView(int resId){
        XmlResourceParser parser = getResources().getLayout(resId);
        mContext.getTheme().applyStyle(resId,true);
        View view = getLayoutInflater().inflate(parser,null);
        setContentView(view);
    }

    public void setContentView(View view){
        ((Activity)mContext).setContentView(view);
    }

//    public void startActivity(Class<? extends PluginActivity> cc,Bundle bundle){
//        PluginActivityHelper.startActivity(mContext,info,cc,bundle);
//    }
//
//    public void startActivity(Class<? extends PluginActivity> cc){
//        PluginActivityHelper.startActivity(mContext,info,cc,null);
//    }
//
//    public void startActivityForResult(Class<? extends PluginActivity> cc,int requestCode,Bundle bundle){
//        PluginActivityHelper.startActivityForResult(mContext,info,cc,requestCode,bundle);
//    }
//
//    public void startActivityForResult(Class<? extends PluginActivity> cc,int requestCode){
//        PluginActivityHelper.startActivityForResult(mContext,info,cc,requestCode,null);
//    }

    public Intent getIntent(){
        return ((Activity) mContext).getIntent();
    }

    public void finish(){
        ((Activity) mContext).finish();
    }

    public View findViewById(int resId){
        View view = ((Activity) mContext).findViewById(resId);
        return view;
    }

    public LayoutInflater getLayoutInflater(){
        return ((Activity)mContext).getLayoutInflater();
    }

    public Application getApplication() {
        return ((Activity) mContext).getApplication();
    }


    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    public Object getSystemService(String name){
        return mContext.getSystemService(name);
    }

    public Activity getCurrentActivity(){
        return (Activity) mContext;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public ComponentName startService(Intent intent){
        return mContext.startService(intent);
    }

    public boolean stopService(Intent intent){
        return mContext.stopService(intent);
    }

    public boolean bindService(Intent intent, ServiceConnection conn,int flags){
        return mContext.bindService(intent, conn, flags);
    }

    public void unbindService(ServiceConnection conn){
        mContext.unbindService(conn);
    }

    public ActionBar getActionBar(){
        return ((Activity) mContext).getActionBar();
    }

    public void setResult(int code,Intent intent){
        ((Activity) mContext).setResult(code, intent);
    }

    public void setResult(int code){
        setResult(code,null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {

        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public Window getWindow(){
        return ((Activity) mContext).getWindow();
    }

    public WindowManager getWindowManager(){
        return ((Activity) mContext).getWindowManager();
    }

    public void startActivity(Intent intent){
        startActivityForResult(intent,-1);
    }

    public void startActivityForResult(Intent intent , int requestCode){
        getCurrentActivity().startActivityForResult(intent,requestCode);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public void setTitle(String title){

        ((Activity)mContext).setTitle(title);
    }

    public CharSequence getTitle(){
        return ((Activity)mContext).getTitle();
    }

    public void registerReceiver(BroadcastReceiver receiver, IntentFilter intentFilter){
        mContext.registerReceiver(receiver,intentFilter);
    }

    public void unregisterReceiver(BroadcastReceiver receiver){
        mContext.unregisterReceiver(receiver);
    }

    public FragmentManager getFragmentManager(){
        return ((Activity)mContext).getFragmentManager();
    }

}
