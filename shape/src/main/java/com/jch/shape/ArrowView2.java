package com.jch.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhanglin on 2017/12/8.
 */

public class ArrowView2 extends View {
    public static final int DEFAULT_HEIGHT = 150;
    public static final int DEFAULT_WIDTH = 800;
    private static final int NONE = -1;
    private static final int MOVE = 0;
    private static final int SCALE = 2;
    private int mode = NONE;
    private Paint rectPaint;
    private Paint highLightPaint;
    private int MIN_LEN = 50;
    private int MIN_LINE_LEN = 20;
    private int rectWidth;
    private int highLightDistance = 50;
    private Path arrowPath, highLightPath;
    PointF E, F, G, H, I, J, K, L, M, N, O, P, Q, R;
    private Rhomboid rect;
    private Rhomboid moveRect;
    private int direction = -1;
    private PointF sp1, sp2, tmp1, tmp2;
    private float lineLen;

    public ArrowView2(Context context) {
        super(context);
        rectPaint = new Paint();
        rectPaint.setColor(Color.RED);
        rectPaint.setAntiAlias(true);
        rectPaint.setStrokeWidth(5);
        rectPaint.setStyle(Paint.Style.FILL);
        highLightPaint = new Paint();
        highLightPaint.setColor(Color.GREEN);
        highLightPaint.setStrokeWidth(5);
        highLightPaint.setStyle(Paint.Style.STROKE);
    }

    protected void initLocation() {
        if (rect != null)
            return;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        PointF A = new PointF((width - DEFAULT_WIDTH) / 2, (height - DEFAULT_HEIGHT) / 2);
        PointF B = new PointF((width + DEFAULT_WIDTH) / 2, (height - DEFAULT_HEIGHT) / 2);
        PointF C = new PointF((width + DEFAULT_WIDTH) / 2, (height + DEFAULT_HEIGHT) / 2);
        PointF D = new PointF((width - DEFAULT_WIDTH) / 2, (height + DEFAULT_HEIGHT) / 2);
        rect = new Rhomboid(A, B, C, D);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initLocation();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Phasor AB = new Phasor(rect.A, rect.B);
        Phasor BA = new Phasor(rect.B, rect.A);
        Phasor AD = new Phasor(rect.A, rect.D);
        Phasor DA = new Phasor(rect.D, rect.A);
        float af = (float) (AD.length() * Math.sin(Math.PI / 3));
        F = rect.A.clone().moveTo(AB, af);
        G = F.clone().moveTo(AD, AD.length() / 3);
        I = rect.B.clone().moveTo(BA, af);
        H = I.clone().moveTo(AD, AD.length() / 3);
        J = rect.B.clone().moveTo(AD, AD.length() / 2);
        K = rect.C.clone().moveTo(BA, af);
        L = K.clone().moveTo(DA, AD.length() / 3);
        M = F.clone().moveTo(AD, AD.length() * 2 / 3);
        N = M.clone().moveTo(AD, AD.length() / 3);
        E = rect.A.clone().moveTo(AD, AD.length() / 2);

        if (arrowPath == null) {
            arrowPath = new Path();
        } else {
            arrowPath.reset();
        }
        arrowPath.moveTo(E.x, E.y);
        arrowPath.lineTo(F.x, F.y);
        arrowPath.lineTo(G.x, G.y);
        arrowPath.lineTo(H.x, H.y);
        arrowPath.lineTo(I.x, I.y);
        arrowPath.lineTo(J.x, J.y);
        arrowPath.lineTo(K.x, K.y);
        arrowPath.lineTo(L.x, L.y);
        arrowPath.lineTo(M.x, M.y);
        arrowPath.lineTo(N.x, N.y);
        arrowPath.close();
        canvas.drawPath(arrowPath, rectPaint);

        O = rect.A.clone().moveTo(DA, highLightDistance).moveTo(BA, highLightDistance);
        P = rect.B.clone().moveTo(AB, highLightDistance).moveTo(DA, highLightDistance);
        Q = P.clone().moveTo(AD, AD.length() + highLightDistance * 2);
        R = O.clone().moveTo(AD, AD.length() + highLightDistance * 2);
        moveRect = new Rhomboid(O, P, Q, R);
        if (highLightPath == null) {
            highLightPath = new Path();
        } else {
            highLightPath.reset();
        }
        highLightPath.moveTo(O.x, O.y);
        highLightPath.lineTo(P.x, P.y);
        highLightPath.lineTo(Q.x, Q.y);
        highLightPath.lineTo(R.x, R.y);
        highLightPath.close();
        rectWidth = (int) AB.length();

        Phasor GH = new Phasor(G,H);
        lineLen = GH.point(AB.getDirectionPhasor());
        //lineLen = new Phasor(G, H).length();
        canvas.drawPath(highLightPath, highLightPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                sp1 = new PointF(event.getX(), event.getY());
                tmp1 = sp1;
                if (moveRect.isInRhomboid(sp1))
                    mode = MOVE;
                else {
                    mode = NONE;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                sp2 = new PointF(event.getX(1), event.getY(1));
                //tmp2 = sp2;
                Phasor S1S2 = new Phasor(sp1, sp2);
                Phasor AC = new Phasor(rect.A, rect.C);
                Phasor BD = new Phasor(rect.B, rect.D);

                float productAC = S1S2.point(AC);
                float productBD = S1S2.point(BD);

                if (Math.abs(productAC) >= Math.abs(productBD)) {
                    if (productAC >= 0) {
                        direction = 0;
                    } else {
                        direction = 1;
                    }
                } else {
                    if (productBD >= 0) {
                        direction = 2;
                    } else {
                        direction = 3;
                    }
                }
                mode = SCALE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MOVE) {
                    float x = event.getX();
                    float y = event.getY();
                    float dx = x - tmp1.x;
                    float dy = y - tmp1.y;
                    tmp1 = new PointF(x, y);
                    Phasor v = new Phasor(dx, dy);
                    rect.move(v);
                    invalidate();

                } else if (mode == SCALE) {
                    if (event.getPointerCount() > 1) {
                        float x1 = event.getX(0);
                        float y1 = event.getY(0);
                        float x2 = event.getX(1);
                        float y2 = event.getY(1);
                        if (tmp2 != null) {
                            Phasor sv = new Phasor(tmp2.x - tmp1.x, tmp2.y - tmp1.y);
                            Phasor ev = new Phasor(x2 - x1, y2 - y1);
                            double radians = sv.angleBy(ev);
                            double angle = Math.toDegrees(radians);
                            radians *= 2;
                            if (angle > 1) {
                                Phasor svXev = sv.x(ev);
                                if (svXev.z > 0) {
                                    radians = -radians;
                                }
                                PointF M = rect.A.clone();
                                Phasor Pac = new Phasor(rect.A, rect.C);
                                Phasor Vac = Pac.getDirectionPhasor();
                                M.moveTo(Vac, Pac.length() / 2);
                                Phasor MA = new Phasor(M, rect.A);
                                Phasor MB = new Phasor(M, rect.B);
                                Phasor MC = new Phasor(M, rect.C);
                                Phasor MD = new Phasor(M, rect.D);
                                MA.rotate(radians);
                                MB.rotate(radians);
                                MC.rotate(radians);
                                MD.rotate(radians);
                                rect.A = M.clone().moveTo(MA);
                                rect.B = M.clone().moveTo(MB);
                                rect.C = M.clone().moveTo(MC);
                                rect.D = M.clone().moveTo(MD);

                            } else {
                                float dx1 = x1 - tmp1.x;
                                float dy1 = y1 - tmp1.y;
                                float dx2 = x2 - tmp2.x;
                                float dy2 = y2 - tmp2.y;

                                Phasor p1 = new Phasor(dx1, dy1);
                                Phasor p2 = new Phasor(dx2, dy2);

                                //判断两个向量分别控制哪个点A,C or B,D
                                Phasor v1 = rect.getWidthDirectionPhasor();
                                Phasor v2 = rect.getHeightDirectionPhasor();
                                float p1v1 = p1.point(v1);
                                float p2v2 = p2.point(v2);
                                float p1v2 = p1.point(v2);
                                float p2v1 = p2.point(v1);

                                boolean scaleWidth = false;
                                boolean scaleHeight = false;
                                Phasor AB = new Phasor(rect.A, rect.B);
                                Phasor AD = new Phasor(rect.A, rect.D);
                                float Lab = AB.length();
                                float Lad = AD.length();
                                if (direction == 0) {
                                    //沿着AC方向放缩
                                    if (Lab > MIN_LEN) {
                                        scaleWidth = true;
                                    } else if (p2v1 - p1v1 > 0) {
                                        scaleWidth = true;
                                    }
                                    if (Lad > MIN_LEN) {
                                        scaleHeight = true;
                                    } else if (p2v2 - p1v2 > 0) {
                                        scaleHeight = true;
                                    }

                                    if(lineLen <= MIN_LINE_LEN){
                                        //h方向放大
                                        if(p1v2 - p2v2 < 0){
                                            scaleHeight = false;
                                        }

                                        else{
                                            scaleHeight = scaleHeight && true;
                                        }
                                        //w方向缩小
                                        if(p1v1 - p2v1 > 0){
                                            scaleWidth = false;
                                        }
                                        else{
                                            scaleWidth = true;
                                        }
                                    }
                                    if (scaleWidth) {
                                        rect.A.moveTo(v1, p1v1, true);
                                        rect.C.moveTo(v1, p2v1, true);
                                        rect.D.moveTo(v1, p1v1, true);
                                        rect.B.moveTo(v1, p2v1, true);
                                    }

                                    if (scaleHeight) {
                                        rect.A.moveTo(v2, p1v2, true);
                                        rect.C.moveTo(v2, p2v2, true);
                                        rect.D.moveTo(v2, p2v2, true);
                                        rect.B.moveTo(v2, p1v2, true);
                                    }

                                } else if (direction == 1) {
                                    //沿着CA方向

                                    if (Lab > MIN_LEN) {
                                        scaleWidth = true;
                                    } else if (p1v1 - p2v1 > 0) {
                                        scaleWidth = true;
                                    }
                                    if (Lad > MIN_LEN) {
                                        scaleHeight = true;
                                    } else if (p1v2 - p2v2 > 0) {
                                        scaleHeight = true;
                                    }
                                    if(lineLen <= MIN_LINE_LEN){
                                        //h方向放大
                                        if(p1v2 - p2v2 > 0){
                                            scaleHeight = false;
                                        }

                                        else{
                                            scaleHeight = scaleHeight && true;
                                        }
                                        //w方向缩小
                                        if(p1v1 - p2v1 < 0){
                                            scaleWidth = false;
                                        }
                                        else{
                                            scaleWidth = true;
                                        }
                                    }
                                    if (scaleWidth) {
                                        rect.A.moveTo(v1, p2v1, true);
                                        rect.B.moveTo(v1, p1v1, true);
                                        rect.C.moveTo(v1, p1v1, true);
                                        rect.D.moveTo(v1, p2v1, true);
                                    }
                                    if (scaleHeight) {
                                        rect.A.moveTo(v2, p2v2, true);
                                        rect.C.moveTo(v2, p1v2, true);
                                        rect.B.moveTo(v2, p2v2, true);
                                        rect.D.moveTo(v2, p1v2, true);
                                    }


                                }
                                //rect.recalculateByAC(rect.A,rect.C,radians);

                                else if (direction == 2) {
                                    //沿着BD方向放缩
                                    if (Lab > MIN_LEN) {
                                        scaleWidth = true;
                                    } else if (p1v1 - p2v1 > 0) {
                                        scaleWidth = true;
                                    }
                                    if (Lad > MIN_LEN) {
                                        scaleHeight = true;
                                    } else if (p2v2 - p1v2 > 0) {
                                        scaleHeight = true;
                                    }
                                    if(lineLen <= MIN_LINE_LEN){
                                        //h方向放大
                                        if(p1v2 - p2v2 < 0){
                                            scaleHeight = false;
                                        }

                                        else{
                                            scaleHeight = scaleHeight && true;
                                        }
                                        //w方向缩小
                                        if(p1v1 - p2v1 < 0){
                                            scaleWidth = false;
                                        }
                                        else{
                                            scaleWidth = true;
                                        }
                                    }
                                    if (scaleWidth) {
                                        rect.B.moveTo(v1, p1v1, true);
                                        rect.D.moveTo(v1, p2v1, true);
                                        rect.C.moveTo(v1, p1v1, true);
                                        rect.A.moveTo(v1, p2v1, true);
                                    }
                                    if (scaleHeight) {
                                        rect.B.moveTo(v2, p1v2, true);
                                        rect.D.moveTo(v2, p2v2, true);
                                        rect.A.moveTo(v2, p1v2, true);
                                        rect.C.moveTo(v2, p2v2, true);
                                    }

                                } else {
                                    //沿着DB方向放缩
                                    if (Lab > MIN_LEN) {
                                    //if(lineLen > MIN_LINE_LEN){
                                        scaleWidth = true;
                                    } else if (p2v1 - p1v1 > 0) {
                                        scaleWidth = true;
                                    }
                                    if (Lad > MIN_LEN) {
                                        scaleHeight = true;
                                    } else if (p1v2 - p2v2 > 0) {
                                        scaleHeight = true;
                                    }
                                    if(lineLen <= MIN_LINE_LEN){
                                        //h方向放大
                                        if(p1v2 - p2v2 > 0){
                                            scaleHeight = false;
                                        }

                                        else{
                                            scaleHeight = scaleHeight && true;
                                        }
                                        //w方向缩小
                                        if(p1v1 - p2v1 > 0){
                                            scaleWidth = false;
                                        }
                                        else{
                                            scaleWidth = true;
                                        }
                                    }
                                    if (scaleWidth) {
                                        rect.B.moveTo(v1, p2v1, true);
                                        rect.D.moveTo(v1, p1v1, true);
                                        rect.C.moveTo(v1, p2v1, true);
                                        rect.A.moveTo(v1, p1v1, true);
                                    }
                                    if (scaleHeight) {
                                        rect.B.moveTo(v2, p2v2, true);
                                        rect.D.moveTo(v2, p1v2, true);
                                        rect.A.moveTo(v2, p2v2, true);
                                        rect.C.moveTo(v2, p1v2, true);
                                    }
                                }

                            }

                            invalidate();
                        }
                        tmp1 = new PointF(x1, y1);
                        tmp2 = new PointF(x2, y2);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                tmp1 = null;
                tmp2 = null;
                mode = NONE;
                direction = -1;
                break;

        }
        return true;
    }

}
