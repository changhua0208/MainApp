package com.jch.shape;

/**
 * y = kx + b
 * @author changhua.jiang
 * @since 2017/12/11 下午7:58
 */

public class LineSegment extends Line {
    static final float MAX_K = 100001;

    public LineSegment(PointF p1, PointF p2){
        super(p1,p2);
    }

    //是否包含交点p点，必须是交点
    public boolean contain(PointF p){
        float maxX= Math.max(p1.x,p2.x);
        float minX = Math.min(p1.x,p2.x);
        if(p.x >= minX && p.x <= maxX){
            return true;
        }
        else{
            return false;
        }
    }

    //对应方程y = kx + b
    public float getK(){
        if(Math.abs(p1.x - p2.x) <= 0.00001){
            return 100001;
        }
        else{
            return (p2.y - p1.y)/(p2.x - p1.x);
        }
    }
    //对应方程y = kx + b
    public float getB(float k){
        return p1.y - k * p1.x;
    }

}
