package com.jch.test;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * @author changhua.jiang
 * @since 2018/1/18 下午5:09
 */

public class TestButton extends Button {
    public TestButton(Context context) {
        super(context);
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources.Theme theme = getContext().getTheme();
        TypedArray ta = theme.obtainStyledAttributes(attrs,R.styleable.TestView,-1,-1);
        String text = ta.getString(R.styleable.TestView_test_text);
        Log.e("TestButton",text);
        ta.recycle();

    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
