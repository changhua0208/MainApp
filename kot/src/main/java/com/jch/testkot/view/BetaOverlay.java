package com.jch.testkot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.jch.test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BetaOverlay extends View {

    private int W_SIZE = 3;
    private int H_SIZE = 4;
    private int MIN_TEXT_SIZE = 60;
    private int MAX_TEXT_SIZE = 120;

    private List<Watermark> watermarkList = new ArrayList<>();
    private boolean isInit = false;
    private TextPaint mPaint;
    private Paint paint;

    public BetaOverlay(Context context) {
        this(context, null);
    }

    public BetaOverlay(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BetaOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new TextPaint();
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BetaOverlayStyle);
        W_SIZE = ta.getInt(R.styleable.BetaOverlayStyle_horizontal_block, 3);
        H_SIZE = ta.getInt(R.styleable.BetaOverlayStyle_vertical_block, 4);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            isInit = true;
            init();
        }

        for (Watermark watermark : watermarkList) {
            drawWaterMark(watermark, canvas);
        }

        //drawWaterMark(watermarkList.get(0), canvas);

    }

    private void drawWaterMark(Watermark watermark, Canvas canvas) {
        canvas.save();
        mPaint.setColor(0x33666666);
        mPaint.setTextSize(watermark.size);
        int x = watermark.position.x;
        int y = watermark.position.y;
        Rect bounds = new Rect();
        mPaint.getTextBounds("Beta", 0, 4, bounds);
        canvas.rotate(watermark.angle, x, y);
        canvas.drawText("Beta", x - bounds.width() / 2, y + bounds.height() / 2, mPaint);
        canvas.restore();
    }

    public int createRandomInt(int min, int max) {
        Random random = new Random();
        //random.setSeed(max + min + System.currentTimeMillis());
        int ret = random.nextInt(max - min) + min;
        return ret;
    }

    public void init() {
        int w = getMeasuredWidth() / W_SIZE;
        int h = getMeasuredHeight() / H_SIZE;
        int len = Math.min(w,h);
        MAX_TEXT_SIZE = len >> 2;
        MIN_TEXT_SIZE = len >> 3;

        for (int i = 0; i < W_SIZE; i++) {
            for (int j = 0; j < H_SIZE; j++) {
                Watermark watermark = createWatermark(i, j);
                if (watermark != null) {
                    watermarkList.add(watermark);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public Point createRandomPoint(int minX, int maxX, int minY, int maxY) {
        int x = createRandomInt(minX, maxX);
        int y = createRandomInt(minY, maxY);
        return new Point(x, y);
    }


    public Watermark createWatermark(int block_x, int block_y) {

        int w = getMeasuredWidth() / W_SIZE;
        int h = getMeasuredHeight() / H_SIZE;
        int x0 = w * block_x;
        int x1 = w * (block_x + 1);
        int y0 = h * block_y;
        int y1 = h * (block_y + 1);
        Point p = createRandomPoint(x0 + (x1 - x0) / 4, x1 - (x1 - x0) / 4, y0 + (y1 - y0) / 4, y1 - (y1 - y0) / 4);
        int size = createRandomInt(MIN_TEXT_SIZE, MAX_TEXT_SIZE);

        Watermark watermark = new Watermark();
        watermark.position = p;
        watermark.size = size;
        watermark.angle = createRandomInt(0, 360);
        return watermark;
    }

    class Watermark {
        Point position;
        int size;
        int angle;
    }

    class Point extends android.graphics.Point {
        Point(int x, int y) {
            super(x, y);
        }

        public double length(Point p) {
            int dx = x - p.x;
            int dy = y - p.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
}
