package com.jch.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author changhua.jiang
 * @since 2017/10/17 上午11:25
 */

public class ShadowRhomboid extends Rhomboid {
    public ShadowRhomboid(PointF A, PointF B, PointF C, PointF D) {
        super(A, B, C, D);
    }

    public void draw(Canvas canvas, Paint paint) {
        ///////////////////////////////////////
        int STEP = 30;
        //Rhomboid rect1 = rect.clone();

        List<Line> lines = calculateLines(STEP);
        for (Line line : lines) {
            line.draw(canvas, paint);
        }
    }

    public List<Line> calculateLines(int STEP){
        //A为原点，AB为x轴,直线分别是x=0，y=0，x=len(AB),y=len(AD) 这四条边与y=-x + i* STEP两个交点连线，多条连线组成阴影
        //再把这些线平移向量OA，旋转α得现在矩形
        Phasor AB = new Phasor(A, B);
        Phasor AD = new Phasor(A, D);
        float lab = AB.length();
        float lad = AD.length();

        Phasor standPhasor = new Phasor(1, -1);
        List<Line> lines = new ArrayList<>();

        int i = 1;
        while (true) {
            //与y=0交点
            Line line = new Line();
            float x = STEP * i;
            float y = 0;
            if (x <= lab) {
                line.add(new PointF(x, y));
            }
            //x = 0
            x = 0;
            y = STEP * i;
            if (y <= lad) {
                line.add(new PointF(x, y));
            }
            //y=lad
            y = lad;
            x = i * STEP - lad;
            if (x <= lab && x >= 0) {
                line.add(new PointF(x, y));
            }
            //x = lab
            x = lab;
            y = i * STEP - lab;
            if (y >= 0 && y <= lad) {
                line.add(new PointF(x, y));
            }
            if (line.isIntegrated()) {
                lines.add(line);
                i++;
            } else {
                break;
            }
        }

        //原点
        PointF O = new PointF(0, 0);
        Phasor v = new Phasor(O, A);
        Phasor xv = new Phasor(1, 0);
        double radians = xv.angleBy(AB);
        if (xv.x(AB).z > 0) {
            radians = -radians;
        }
        for (Line line : lines) {
            float ret = line.getDirection().point(standPhasor);
            if (ret < 0) {
                line.reverse();
            }
            line.rotate(O, radians);
            line.moveTo(v);
        }
        return lines;
    }

}
