package com.jch.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jch.plugin.PluginActivity;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends PluginActivity {
    @BindView(R.id.btn1)
    Button mBtn1;
    @BindView(R.id.btn2)
    Button mBtn2;
    @BindView(R.id.btn3)
    Button mBtn3;
    @BindView(R.id.btn4)
    Button mBtn4;

    @BindString(R.string.s1) String s1;

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this,this.getCurrentActivity());
        Toast.makeText(getCurrentActivity(), s1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn1)
    public void startAnActivity(){
        Intent intent = new Intent(getCurrentActivity(), SecandActivity.class);
        startActivityForResult(intent, 100);
    }
    @OnClick(R.id.btn2)public void printLogByNative(){
        Logger.e("HAHAHA", "_________________");
    }
    @OnClick(R.id.btn3) public void toSomething(){

    }

    @OnClick(R.id.btn4)
    public void startActivityByScheme(){
        Intent intent1 = new Intent();
        intent1.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("plugin://changhua.jiang/test");
        intent1.setData(uri);
        startActivity(intent1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String ret = data.getStringExtra("RET");
            Toast.makeText(getCurrentActivity(), ret, Toast.LENGTH_SHORT).show();
        }
    }
}
