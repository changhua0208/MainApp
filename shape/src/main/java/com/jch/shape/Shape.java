package com.jch.shape;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * @author changhua.jiang
 * @since 2017/10/16 上午11:10
 */

public interface Shape {

    //public b

    public boolean onTouchEvent(MotionEvent event);

    public boolean isInner(float x, float y);

    public void draw(Canvas canvas);

    public void setHighlight();
}
