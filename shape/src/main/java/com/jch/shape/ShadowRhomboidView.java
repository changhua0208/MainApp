package com.jch.shape;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author changhua.jiang
 * @since 2017/10/17 下午12:01
 */

public class ShadowRhomboidView extends RhomboidView {
    public ShadowRhomboidView(Context context) {
        super(context);
    }

    public ShadowRhomboidView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowRhomboidView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initRhomboid() {
        if(rect == null){
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            PointF A = new PointF((width-DEFAULT_LEN) >> 1 , (height-DEFAULT_LEN) >>1);
            PointF B = new PointF((width+DEFAULT_LEN) >> 1, (height-DEFAULT_LEN) >>1);
            PointF C = new PointF((width+DEFAULT_LEN) >> 1, (height+DEFAULT_LEN) >>1);
            PointF D = new PointF((width-DEFAULT_LEN) >> 1, (height+DEFAULT_LEN) >>1);
            rect = new ShadowRhomboid(A, B, C, D);
        }
    }
}
