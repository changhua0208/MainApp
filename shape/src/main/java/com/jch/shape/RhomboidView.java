package com.jch.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * @author changhua.jiang
 * @since 2017/10/11 下午4:57
 */

public class RhomboidView extends View {
    private static final int NONE = -1;
    private static final int MOVE = 0;
    private static final int SCALE = 2;
    private Paint paint;
    //private RectF rect;
    protected Rhomboid rect;
    private PointF sp1;
    private PointF sp2;

    private PointF tmp1;
    private PointF tmp2;

    private int direction = -1;
    private int mode = MOVE;
    private int MIN_LEN = 100;
    public static final int DEFAULT_LEN = 300;


    public RhomboidView(Context context) {
        this(context, null);
    }

    public RhomboidView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RhomboidView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(0xff7f7f7f);
        paint.setAntiAlias(true);
        PathEffect effects = new DashPathEffect(new float[]{20, 20, 20, 20}, 5);
        paint.setPathEffect(effects);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawRect(rect,paint);
        rect.draw(canvas, paint);
        super.onDraw(canvas);
    }

    protected void initRhomboid(){
        if(rect == null){
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            PointF A = new PointF((width-DEFAULT_LEN) >> 1 , (height-DEFAULT_LEN) >>1);
            PointF B = new PointF((width+DEFAULT_LEN) >> 1, (height-DEFAULT_LEN) >>1);
            PointF C = new PointF((width+DEFAULT_LEN) >> 1, (height+DEFAULT_LEN) >>1);
            PointF D = new PointF((width-DEFAULT_LEN) >> 1, (height+DEFAULT_LEN) >>1);
            rect = new Rhomboid(A, B, C, D);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initRhomboid();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                sp1 = new PointF(event.getX(), event.getY());
                tmp1 = sp1;
                if (rect.isInRhomboid(sp1))
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
//                            Phasor diagonal = null;
//                            if(direction > 1){
//                                diagonal = new Phasor(rect.B,rect.D);
//                            }
//                            else {
//                                diagonal = new Phasor(rect.A,rect.C);
//                            }
//                            Phasor p = diagonal.getPerpendicularPhasor().getDirectionPhasor();
//                            double result = Math.abs(p.point(p1) - p.point(p2));
//                            Log.e("liju>","liju is " + p.point(p1) + "   " + p.point(p2));

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
                                        scaleWidth = true;
                                    } else if (p2v1 - p1v1 > 0) {
                                        scaleWidth = true;
                                    }
                                    if (Lad > MIN_LEN) {
                                        scaleHeight = true;
                                    } else if (p1v2 - p2v2 > 0) {
                                        scaleHeight = true;
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
                                //rect.recalculateByBD(rect.B,rect.D,Math.PI / 2 - radians);

                            }

//                            if(sp1.x > sp2.x && sp1.y > sp2.y){
//                                //p1控制C
//                                //p2控制A
//                                rect.top += p2.y;
//                                rect.left += p2.x;
//                                rect.right += p1.x;
//                                rect.bottom += p1.y;
//                            }
//                            else if(sp1.x < sp2.x && sp1.y < sp2.y){
//                                //P1 A P2 C
//                                rect.left += p1.x;
//                                rect.top += p1.y;
//                                rect.right += p2.x;
//                                rect.bottom += p2.y;
//                            }
//                            else if(sp1.x > sp2.x && sp1.y < sp2.y){
//                                //p1 B p2 D
//                                rect.right += p1.x;
//                                rect.top += p1.y;
//                                rect.left += p2.x;
//                                rect.bottom += p2.y;
//                            }
//                            else{
//                                //p1 D p2 B
//                                rect.left += p1.x;
//                                rect.bottom += p1.y;
//                                rect.right += p2.x;
//                                rect.top += p2.y;
//                            }

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
//        if(event.getPointerCount() > 1) {
//            String s = "P1(%f,%f)  P2(%f,%f)";
//            Log.e("PPP",String.format(s,sp1.x,sp1.y,sp2.x,sp2.y));
//            Log.e("ddd",String.format(s,event.getX(0),event.getY(0),event.getX(1),event.getY(1)));
//        }
        boolean ret = super.onTouchEvent(event);
        return ret;
    }

    public Rhomboid getRhomboid(){
        return rect;
    }


}
