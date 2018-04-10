package com.jch.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Rhomboid implements Cloneable {
    public PointF A;
    public PointF B;
    public PointF C;
    public PointF D;

    public Rhomboid(PointF A, PointF B, PointF C, PointF D) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }

    public Phasor getWidthDirectionPhasor() {
        Phasor p = new Phasor(A, B);
        return p.getDirectionPhasor();
    }

    public Phasor getHeightDirectionPhasor() {
        Phasor p = new Phasor(A, D);
        return p.getDirectionPhasor();
    }

    public boolean isInRhomboid(PointF M) {
        if (isAcuteAngle(A, B, D, M) && isAcuteAngle(B, A, C, M) && isAcuteAngle(C, B, D, M) && isAcuteAngle(D, A, C, M)) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas, Paint paint) {
        Path path = new Path();
        path.moveTo(A.x, A.y);
        path.lineTo(B.x, B.y);
        path.lineTo(C.x, C.y);
        path.lineTo(D.x, D.y);
        path.close();
        canvas.drawPath(path, paint);
    }


    //P1P2 P1P3 与 P1M 三个向量点积为正数
    private boolean isAcuteAngle(PointF P1, PointF P2, PointF P3, PointF M) {
        Phasor P1P2 = new Phasor(P1, P2);
        Phasor P1P3 = new Phasor(P1, P3);
        Phasor P1M = new Phasor(P1, M);
        if (P1P2.point(P1M) > 0 && P1P3.point(P1M) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void recalculateByAC(PointF A, PointF C, Double angle) {
        //Double angle = getAngle();
        double cost = Math.cos(angle);
        double sint = Math.sin(angle);
        Phasor v0 = new Phasor(A, C);
        //AB方向向量
        Phasor v = new Phasor();
        Phasor v1 = v0.getDirectionPhasor();
        v.x = (float) (v1.x * cost - v1.y * sint);
        v.y = (float) (v1.x * sint + v1.y * cost);
        //A沿着AB方向位移lenght(AB)
        double len = v0.length() * cost;
        B.x = (float) (A.x + v.x * len);
        B.y = (float) (A.y + v.y * len);
        //C沿着CD方向位移lenght(CD)
        v.reverse();
        D.x = (float) (C.x + v.x * len);
        D.y = (float) (C.y + v.y * len);
        this.A = A;
        this.C = C;
    }

    public void recalculateByBD(PointF B, PointF D, Double angle) {
        //Double angle = getAngle();
        double cost = Math.cos(angle);
        double sint = Math.sin(angle);
        Phasor v0 = new Phasor(B, D);
        //BC方向向量
        Phasor v = new Phasor();
        Phasor v1 = v0.getDirectionPhasor();
        v.x = (float) (v1.x * cost - v1.y * sint);
        v.y = (float) (v1.x * sint + v1.y * cost);
        //B沿着BC方向位移lenght(BC)
        double len = v0.length() * cost;
        this.C.x = (float) (B.x + v.x * len);
        this.C.y = (float) (B.y + v.y * len);
        //D沿着DA方向位移lenght(AD)
        v.reverse();
        this.A.x = (float) (D.x + v.x * len);
        this.A.y = (float) (D.y + v.y * len);
        this.B = B;
        this.D = D;
    }

    //获得AB AC夹角
    private Double getAngle() {
        Phasor AB = new Phasor(A, B);
        Phasor AC = new Phasor(A, C);
        //|AB|*|AC|*cosx
        return AB.angleBy(AC);
    }

    public void move(Phasor v) {
        A.x += v.x;
        A.y += v.y;
        B.x += v.x;
        B.y += v.y;
        C.x += v.x;
        C.y += v.y;
        D.x += v.x;
        D.y += v.y;
    }

    public Rhomboid clone() {
        PointF p1 = A.clone();
        PointF p2 = B.clone();
        PointF p3 = C.clone();
        PointF p4 = D.clone();
        return new Rhomboid(p1, p2, p3, p4);
    }

}