package com.jch.shape;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author changhua.jiang
 * @since 2017/12/11 下午3:13
 */

public class FloatingButton extends ImageView {

    private boolean processFlag = true;
    private OnProcessCallback callback;

    public FloatingButton(Context context) {
        super(context);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public FloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

    private Runnable processRunnable = new Runnable() {
        @Override
        public void run() {
//            if(processFlag){
                if(callback != null){
                    callback.onProcess();
                }
                handler.postDelayed(this,100);
//            }
        }
    };

    public void setCallback(OnProcessCallback callback) {
        this.callback = callback;
    }

    private Handler handler = new Handler();

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch ((ev.getAction() & MotionEvent.ACTION_MASK)){
            case MotionEvent.ACTION_DOWN:
//                processFlag = true;
                handler.postDelayed(processRunnable,100);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                processFlag = false;
                handler.removeCallbacks(processRunnable);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnProcessCallback{

        public void onProcess();

    }
}
