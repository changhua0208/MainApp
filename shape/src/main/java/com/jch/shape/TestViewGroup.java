package com.jch.shape;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author changhua.jiang
 * @since 2017/10/25 下午9:15
 */

public class TestViewGroup extends ViewGroup {
    private static String TAG = "TestViewGroup";
    public TestViewGroup(Context context) {
        super(context);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, String.format("changed %s,l %d,t %d,r %d,b %d", String.valueOf(changed),l,t,r,b));

        //if(changed){
            //Log.e(TAG,String.format("changed %s,l %d,t %d,r %d,b %d",String.valueOf(changed),l,r,t,b));
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                int h = view.getMeasuredHeight();
                int w = view.getMeasuredWidth();
                view.layout(0,0,w,h);
            }
        //}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }

    }
}
