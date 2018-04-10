package com.jch.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author changhua.jiang
 * @since 2017/12/8 下午12:02
 */

public class PolygonView extends View {
    private Paint mPaint = new Paint();

    private Polygon polygon = new ShadowPolygon();
    private Polygon.Vertex selection = null;
    private PointF tmpPoint = null;

    public PolygonView(Context context) {
        this(context,null);
    }

    public PolygonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PolygonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        polygon.draw(canvas,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            {
                if(polygon.getEditable()) {
                    float x = event.getX();
                    float y = event.getY();
                    selection = polygon.selectVertex(x, y);
                    if (selection == null) {
                        polygon.addVertex(x, y);
                    }
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                if(polygon.getEditable()) {
                    float x = event.getX();
                    float y = event.getY();
                    if (selection != null) {
                        if (tmpPoint != null) {
                            float dx = event.getX() - tmpPoint.x;
                            float dy = event.getY() - tmpPoint.y;
                            selection.x += dx;
                            selection.y += dy;
                            tmpPoint.x = x;
                            tmpPoint.y = y;
                            invalidate();
                        } else {
                            tmpPoint = new PointF(x, y);
                        }
                    }
                }
                else{
                    if(polygon.isInPolygonRect(event.getX(),event.getY())) {
                        if (tmpPoint == null) {
                            tmpPoint = new PointF(event.getX(), event.getY());
                        } else {
                            float dx = event.getX() - tmpPoint.x;
                            float dy = event.getY() - tmpPoint.y;
                            tmpPoint = new PointF(event.getX(), event.getY());
                            Phasor phasor = new Phasor(dx, dy);
                            polygon.moveTo(phasor);
                            invalidate();
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            {
                //selection = null;
                tmpPoint = null;
            }
        }
        return super.onTouchEvent(event);
    }

    public void rollback(){
        polygon.deleteLastPoint();
        invalidate();
    }

    public void setEditable(boolean editable){
        polygon.setEditable(editable);
        invalidate();
    }

}
