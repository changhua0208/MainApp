package com.jch.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author changhua.jiang
 * @since 2017/12/8 下午5:47
 */

public class ShadowPolygon extends Polygon {
    private List<Line> lines;
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(editable) {
            super.draw(canvas, paint);
        }
        //至少三个顶点才能做出一个多边形
        if(vertices.size() >= 3) {
            if(editable) {
                lines = calculateLines(30);
            }
            if (lines != null) {
                for (Line l : lines) {
                    l.draw(canvas, paint);
                }
            }
        }
    }

    //计算阴影
    public List<Line> calculateLines(int STEP){
        //以安全矩形的左上角为原点，进行切割,切割刀右下角完毕，
        int endX = (int) Math.ceil(safeRect.left + safeRect.top + safeRect.width() + safeRect.height());
        int startX = (int) (safeRect.left + safeRect.top + STEP);
        List<List<Intersection>> ls = new ArrayList<>();
        for (int c = startX ; c < endX; c += STEP) {
            List<Intersection> intersections = new ArrayList<>();
            for (int i = 0; i < vertices.size() ; i++) {
                LineSegment l = new LineSegment(vertices.get(i % vertices.size()),vertices.get((i + 1) % vertices.size()));
                float k = l.getK();
                float b = l.getB(k);
                float x = 0;
                float y = 0;
                if(k < LineSegment.MAX_K) {
                    x = (c - b) / (k + 1);
                    y = (b - c) / (k + 1) + c;
                }
                else{
                    x = l.p1.x;
                    y = -x + c;
                }

                Intersection intersection = new Intersection(x,y);
                if(l.contain(intersection)){
                    intersections.add(intersection);
                }
            }
            if(intersections.size() > 1) {
                //中间与多边形的交点有n个，对交点进行排序
                Collections.sort(intersections);
                ls.add(intersections);
            }
        }

        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < ls.size(); i++) {
            List<Intersection> intersections = ls.get(i);
            for (int j = 0; j < intersections.size() - 1; j++) {
                Intersection p1 = intersections.get(j);
                Intersection p2 = intersections.get(j + 1);
                float midX = (p1.x + p2.x) / 2;
                float midY = (p1.y + p2.y) / 2;
                PointF p = new PointF(midX,midY);
                //通过两个交点的中间点是否在多边形中间来确定该线是否是阴影需要线段。
                if(isPolygonContainsPoint(p)){
                    lines.add(new Line(p1,p2));
                }
            }
        }
        return lines;
    }

    class Intersection extends PointF implements Comparable<Intersection> {

        public Intersection(float x, float y) {
            super(x, y);
        }

        public Intersection(float x, float y, float z) {
            super(x, y, z);
        }

        @Override
        public int compareTo(@NonNull Intersection i) {
            if(i.x > x){
                return -1;
            }
            else if(i.x < x){
                return 1;
            }
            else return 0;
        }
    }

    @Override
    public void moveTo(Phasor phasor){
        super.moveTo(phasor);
        for(Line line : lines){
            line.moveTo(phasor);
        }
    }
}
