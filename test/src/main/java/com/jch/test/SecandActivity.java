package com.jch.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.jch.plugin.BaseActivity;

/**
 * @author changhua.jiang
 * @since 2017/9/25 下午3:00
 */

public class SecandActivity extends BaseActivity implements View.OnClickListener {
    private Button btnBack;

//    public SecandActivity(Context context, PluginInfo info) {
//        super(context, info);
//    }

    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.act_secand);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        findViewById(R.id.btn_keyboard).setOnClickListener(this);
        findViewById(R.id.btn_start_proxy).setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("Bundle");
        if (bundle != null) {
            String msg = bundle.getString("Key");
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBack(View view) {
        Intent intent = new Intent();
        intent.putExtra("RET", "onBack");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBack(v);
                break;
            case R.id.btn_keyboard:
                onKeyboard();
                break;
            case R.id.btn_start_proxy:
                onStartProxy();
            default:
        }
    }

    private void onStartProxy() {
        Intent intent = new Intent(this,ThirdActivity.class);
        startActivity(intent);
    }

    private void onKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("HAHA", event.toString());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.e("HAHA", event.toString());
        return super.onKeyUp(keyCode, event);
    }
}
