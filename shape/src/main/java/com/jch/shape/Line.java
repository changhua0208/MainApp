package com.jch.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

//对应方程y = kx + b
class Line {
    PointF p1;
    PointF p2;

    public Line(PointF p1, PointF p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Line() {
    }

    public boolean isIntegrated() {
        return p1 != null && p2 != null;
    }

    public void add(PointF p) {
        if (p1 == null) {
            p1 = p;
            return;
        }
        if (p2 == null) {
            p2 = p;
            return;
        }
    }

    public Phasor getDirection() {
        return new Phasor(p1, p2);
    }

    public void moveTo(Phasor v) {
        p1.moveTo(v);
        p2.moveTo(v);
    }

    public void rotate(PointF O, double r) {
        Phasor v1 = new Phasor(O, p1);
        v1.rotate(r);
        p1 = O.clone().moveTo(v1);
        Phasor v2 = new Phasor(O, p2);
        v2.rotate(r);
        p2 = O.clone().moveTo(v2);
    }

    public void reverse() {
        PointF tmp = p1;
        p1 = p2;
        p2 = tmp;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
    }

}