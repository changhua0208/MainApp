package com.jch.shape;

public class Phasor implements Cloneable {
    float x;
    float y;
    //z 屏幕内外方向
    float z;

    public Phasor() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Phasor(float dx, float dy) {
        this(dx, dy, 0f);
    }

    public Phasor(float dx, float dy, float dz) {
        this.x = dx;
        this.y = dy;
        this.z = dz;
    }

    //p1->p2
    public Phasor(PointF p1, PointF p2) {
        this.x = p2.x - p1.x;
        this.y = p2.y - p1.y;
        this.z = p2.z - p1.z;
    }

    //向量点积
    public float point(Phasor p) {
        return p.x * x + p.y * y + p.z * z;
    }

    //向量叉积
    public Phasor x(Phasor v) {
        float x1 = y * v.z - z * v.y;
        float y1 = z * v.x - x * v.z;
        float z1 = x * v.y - y * v.x;
        return new Phasor(x1, y1, z1);
    }

    public Phasor getDirectionPhasor() {
        float d = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        Phasor p = new Phasor(x / d, y / d);
        return p;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void reverse() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public double angleBy(Phasor v) {
        float cosx = getDirectionPhasor().point(v.getDirectionPhasor());
        double radians = Math.acos(cosx);
        return radians;
    }

    @Override
    public String toString() {
        String str = "(x=%f,y=%f)";
        return String.format(str, x, y);
    }

    public Phasor clone() {
        return new Phasor(x, y, z);
    }

    //在同屏幕旋转
    public void rotate(double radians) {
        double sint = Math.sin(radians);
        double cost = Math.cos(radians);
        float x1 = (float) (x * cost + y * sint);
        float y1 = (float) (y * cost - x * sint);
        this.x = x1;
        this.y = y1;
    }

    public Phasor getPerpendicularPhasor(){
        return new Phasor(y,x);
    }
}