package com.jch.shape;

public class PointF extends android.graphics.PointF implements Cloneable {
    public float z;

    public PointF(float x, float y) {
        this(x, y, 0f);
    }

    public PointF(float x, float y, float z) {
        super(x, y);
        this.z = z;
    }

    public PointF moveTo(Phasor v, float len) {
        return moveTo(v, len, false);
    }

    public PointF moveTo(Phasor v, float len, boolean isDirectionPhasor) {
        if (isDirectionPhasor) {
            this.x = this.x + v.x * len;
            this.y = this.y + v.y * len;
        } else {
            Phasor v0 = v.getDirectionPhasor();
            this.x = this.x + v0.x * len;
            this.y = this.y + v0.y * len;
        }
        return this;
    }

    public PointF moveTo(Phasor v) {
        this.x = this.x + v.x;
        this.y = this.y + v.y;
        return this;
    }

    public PointF clone() {
        return new PointF(x, y);
    }

    public String toString(){
        String str = "(%f,%f,%f)";
        return String.format(str,x,y,z);
    }

}