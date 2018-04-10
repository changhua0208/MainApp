package com.jch.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jch.plugin.BaseActivity;

/**
 * @author changhua.jiang
 * @since 2018/2/5 下午5:21
 */

public class FourthActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fourth);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back){
            finish();
        }
    }
}
