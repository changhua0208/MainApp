package com.jch.test2;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jch.plugin.PluginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/7/9 下午7:08
 */

public class MainActivity extends PluginActivity {
    @BindView(R.id.vp_container)
    ViewPager mViewPager;

    MyPagerAdapter mPagerAdapter;

    int currentIndex = 0;
    View mCurrentButton;

    View mMineText;
    @BindView(R.id.rl_msg)
    View mBtnMsg;
    @BindView(R.id.rl_work)
    View mBtnWork;
    @BindView(R.id.rl_setting)
    View mBtnSetting;
    @BindView(R.id.rl_mine)
    View mBtnMine;

    List<Fragment> fs = new ArrayList<>(4);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this,getCurrentActivity());
        mCurrentButton = mBtnMsg;
        onMsgSelected();
        fs.add(new MsgFragment());
        fs.add(new MsgFragment());
        fs.add(new MsgFragment());
        fs.add(new MsgFragment());
        Class<?> clazz = null;
        try {
            clazz = getCurrentActivity().getClassLoader().loadClass("android.support.v4.view.PagerAdapter");

        } catch (Throwable e) {
            e.printStackTrace();
        }

        mPagerAdapter = new MyPagerAdapter(getFragmentManager(),fs);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0,false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                switch (position){
                    case 0:
                        onMsgSelected();
                        break;
                    case 1:
                        onWorkSelected();
                        break;
                    case 2:
                        onSettingSelected();
                        break;
                    case 3:
                        onMineSelected();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Log.e("xxxooo","result is " + ContextCompat.checkSelfPermission(getCurrentActivity(), Manifest.permission_group.LOCATION));
    }

    @OnClick(R.id.img_find)
    public void onFind(){
        toast("还没有实现");
    }

    @OnClick(R.id.img_scan)
    public void onScan(){
        toast("还没有实现");
    }

    public void toast(String msg){
        Toast.makeText(getCurrentActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rl_msg)
    public void onMsgSelected(){
        mCurrentButton.setSelected(false);
        mBtnMsg.setSelected(true);
        mCurrentButton = mBtnMsg;
        if(currentIndex != 0)
            mViewPager.setCurrentItem(0,false);
    }

    @OnClick(R.id.rl_setting)
    public void onSettingSelected(){
        mCurrentButton.setSelected(false);
        mBtnSetting.setSelected(true);
        mCurrentButton = mBtnSetting;
        if(currentIndex != 2)
        mViewPager.setCurrentItem(2,false);
    }

    @OnClick(R.id.rl_work)
    public void onWorkSelected(){
        mCurrentButton.setSelected(false);
        mBtnWork.setSelected(true);
        mCurrentButton = mBtnWork;
        if(currentIndex != 1)
            mViewPager.setCurrentItem(1,false);
    }

    @OnClick(R.id.rl_mine)
    public void onMineSelected(){
        mCurrentButton.setSelected(false);
        mBtnMine.setSelected(true);
        mCurrentButton = mBtnMine;
        if(currentIndex != 4)
            mViewPager.setCurrentItem(4,false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
