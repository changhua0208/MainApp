package com.jch.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author changhua.jiang
 * @since 2017/12/8 下午4:10
 */

public class Polygon {
    protected List<Vertex> vertices = new LinkedList<>();
    private Paint mPaint = new Paint();
    private int MAX_VERTEX_SELECTION = 60;
    //多边形的安全矩形
    protected RectF safeRect = new RectF();
    protected boolean editable = true;

    public Polygon(){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(4);
    }

    public void addVertex(float x,float y){
        Vertex vertex = new Vertex(x,y);
        vertex.setSelection(true);
        for (Vertex v :
                vertices) {
            v.setSelection(false);
        }
        vertices.add(vertex);

    }

    public void recreateSafeRect(){
        if(vertices.size() > 0) {
            Vertex v = vertices.get(0);
            safeRect.left = v.x;
            safeRect.right = v.x;
            safeRect.top = v.y;
            safeRect.bottom = v.y;
            for (int i = 1; i < vertices.size(); i++) {
                Vertex v0 = vertices.get(i);
                safeRect.left = Math.min(v0.x, safeRect.left);
                safeRect.right = Math.max(v0.x, safeRect.right);
                safeRect.top = Math.min(v0.y, safeRect.top);
                safeRect.bottom = Math.max(v0.y, safeRect.bottom);
            }
        }
    }

    public Vertex selectVertex(float x,float y){
        Vertex selection = null;
        for (Vertex v :
                vertices) {
            Phasor phasor = new Phasor(v,new PointF(x,y));
            if(phasor.length() <= MAX_VERTEX_SELECTION && selection == null){
                v.setSelection(true);
                selection = v;
            }
            else{
                v.setSelection(false);
            }
        }
        return selection;
    }

    public class Vertex extends PointF {
        private boolean isSelected = false;
        public void setSelection(boolean isSelected){
            this.isSelected = isSelected;
        }

        public Vertex(float x, float y) {
            super(x, y);
        }

        public Vertex(float x, float y, float z) {
            super(x, y, z);
        }

        public void draw(Canvas canvas, Paint paint){
            if(isSelected) {
                canvas.drawCircle(x, y, 10, mPaint);
            }
            else{
                canvas.drawCircle(x, y, 10, paint);
            }
        }

    }

    public void draw(Canvas canvas, Paint paint){
        recreateSafeRect();
        Path path = new Path();
        if(vertices.size() > 0){
            Vertex startPoint = vertices.get(0);
            startPoint.draw(canvas,paint);
            path.moveTo(startPoint.x,startPoint.y);
        }
        for(int i = 1;i < vertices.size();i++){
            Vertex point = vertices.get(i);
            point.draw(canvas,paint);
            path.lineTo(point.x,point.y);
        }
        path.close();
        canvas.drawPath(path,paint);
    }

    public void deleteLastPoint(){
        if(vertices.size() > 0){
            vertices.remove(vertices.size() - 1);
        }
    }

    public boolean isPolygonContainsPoint(PointF point){
        return isPolygonContainsPoint(vertices,point);
    }

    /**
     * 返回一个点是否在一个多边形区域内
     * @param point   待判断点
     * @return true 多边形包含这个点,false 多边形未包含这个点。
     */
    public boolean isPolygonContainsPoint(List<Vertex> vertices, PointF point) {
        int nCross = 0;
        for (int i = 0; i < vertices.size(); i++) {
            PointF p1 = vertices.get(i);
            PointF p2 = vertices.get((i + 1) % vertices.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数
            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.y == p2.y)
                continue;
            // point 在p1p2 底部 --> 无交点
            if (point.y < Math.min(p1.y, p2.y))
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.y >= Math.max(p1.y, p2.y))
                continue;
            // 求解 point点水平线与当前p1p2边的交点的 X 坐标
            double x = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
            if (x > point.x) // 当x=point.x时,说明point在p1p2线段上
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    public int getVertexSize(){
        return vertices.size();
    }

    public void setEditable(boolean editable){
        this.editable = editable;
    }

    public boolean getEditable(){
        return this.editable;
    }

    public void moveTo(Phasor phasor){
        for(Vertex vertex : vertices){
            vertex.moveTo(phasor);
        }
        recreateSafeRect();
    }

    public boolean isInPolygonRect(float x,float y){

        List<Vertex> rectPoints = new ArrayList<>();
        float x1 = safeRect.left ;
        float y1 = safeRect.top;
        float x2 = safeRect.left + safeRect.width();
        float y2 = y1;
        float x3 = x2;
        float y3 = y2 + safeRect.height();
        float x4 = x1;
        float y4 = y3;
        rectPoints.add(new Vertex(x1,y1));
        rectPoints.add(new Vertex(x2,y2));
        rectPoints.add(new Vertex(x3,y3));
        rectPoints.add(new Vertex(x4,y4));
        return isPolygonContainsPoint(rectPoints,new PointF(x,y));
    }

}
