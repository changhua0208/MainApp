package com.jch.test;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jch.plugin.PluginActivity;
import com.jch.plugin.model.AXmlActivityInfo;
import com.jch.plugin.model.AXmlElement;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends PluginActivity implements View.OnClickListener {

    private Button mBtn1, mBtn2, mBtn3, mBtn4;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getCurrentActivity(), getResources().getString(R.string.s1), Toast.LENGTH_SHORT).show();

        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);
        mBtn4 = (Button) findViewById(R.id.btn4);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn1:
                Intent intent = new Intent(getCurrentActivity(), SecandActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn2:
                Logger.e("HAHAHA", "_________________");
                break;
            case R.id.btn3:
                //Activity全屏显示，且状态栏被隐藏覆盖掉。
            {
                AssetManager mgr = getAssets();
                try {
                    InputStream in = mgr.open("test.xml");
                    XmlPullParser xmlParser = Xml.newPullParser();
                    xmlParser.setInput(in, "utf-8");
                    int event = xmlParser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT) {
                        switch (event) {
                            case XmlPullParser.START_DOCUMENT:
                                Log.e(TAG, "START_DOCUMENT");
                                break;
                            case XmlPullParser.START_TAG:
                                //一般都是获取标签的属性值，所以在这里数据你需要的数据
                                if (AXmlElement.ACTIVITY_TAG.equals(xmlParser.getName())) {
                                    AXmlActivityInfo activityInfo = AXmlActivityInfo.resolve(xmlParser);
                                    Log.e(TAG, activityInfo.toString());
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                Log.e(TAG, "END_TAG");
                                break;
                            default:
                                break;
                        }
                        event = xmlParser.next();   //将当前解析器光标往下一步移
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
//                int flag1 = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                window.setFlags(flag1, flag1);
            break;
            case R.id.btn4: {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("plugin://changhua.jiang/test");
                intent1.setData(uri);
                startActivity(intent1);
                break;
            }

        }
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
