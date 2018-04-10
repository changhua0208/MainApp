package com.jch.testkot.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 *
 * @author changhua.jiang
 * @since 2018/3/7 下午2:59
 */
class ScanFrameView : View {

    companion object {
        val TAG : String = "ScanFrameView"
    }

    var frame_height = 560
    var frame_stroke_color = Color.parseColor("#eeeeee")
    var corner_color = Color.BLUE
    var mPaint = Paint()
    var corner_lenght = frame_height / 8

    constructor(context : Context) : this(context,null){

    }

    constructor(context:Context,attrs:AttributeSet?) : this(context,attrs,-1){

    }

    constructor(context:Context,attrs:AttributeSet?,defStyleAttrs : Int) : super(context,attrs,defStyleAttrs){
        mPaint.color = frame_stroke_color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {

        val width = measuredWidth
        val height = measuredHeight
        if(width < frame_height || height < frame_height){
            return
        }
        val startX = ((width - frame_height) / 2).toFloat()
        val endX = (startX + frame_height).toFloat()
        val startY = ((height - frame_height) / 2).toFloat()
        val endY = (startY + frame_height).toFloat()
        val rect = Rect(startX.toInt(),startY.toInt(),endX.toInt(),endY.toInt())
        canvas.drawRect(rect,mPaint)
        val path = Path()
        path.moveTo(startX, (startY + corner_lenght))
        path.lineTo(startX,startY)
        path.lineTo((startX + corner_lenght),startY)
        mPaint.color = corner_color
        mPaint.strokeWidth = 6f
        canvas.drawPath(path,mPaint)

        canvas.save()
        canvas.translate(frame_height.toFloat(),0f)
        canvas.rotate(90.0f,startX,startY)
        canvas.drawPath(path,mPaint)
        canvas.restore()

        canvas.save()
        canvas.translate(0f,frame_height.toFloat())
        canvas.rotate(-90.0f,startX,startY)
        canvas.drawPath(path,mPaint)
        canvas.restore()

        canvas.save()
        canvas.translate(frame_height.toFloat(),frame_height.toFloat())
        canvas.rotate(180f,startX,startY)
        canvas.drawPath(path,mPaint)
        canvas.restore()

    }

}