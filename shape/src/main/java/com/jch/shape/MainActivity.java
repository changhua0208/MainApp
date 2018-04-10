package com.jch.shape;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jch.plugin.PluginActivity;

public class MainActivity extends PluginActivity {
    PolygonView polygonView;
    FloatingButton btn;
    FloatingButton btnFinish;
    boolean editable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ArrowView2 arrowView2 = new ArrowView2(this);
//        setContentView(arrowView2);
        Log.e("HAHAHA",FloatingButton.class.getClassLoader().toString());
        Log.e("HAHAHA",findViewById(R.id.btn_goback).getClass().getClassLoader().toString());
        btn = (FloatingButton) findViewById(R.id.btn_goback);
        btnFinish = (FloatingButton) findViewById(R.id.btn_finish);
        polygonView = (PolygonView) findViewById(R.id.v_polygon);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polygonView.rollback();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable = !editable;
                polygonView.setEditable(editable);
            }
        });

//        btn.setCallback(new FloatingButton.OnProcessCallback() {
//            @Override
//            public void onProcess() {
//                Log.e("MainActivity","hahaha");
//            }
//        });
    }

}
