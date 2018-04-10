package com.jch.testkot.camera

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.Parameters.FOCUS_MODE_AUTO
import android.os.Handler
import android.util.Log
import android.view.SurfaceView
import com.jch.utils.ScreenUtils

/**
 *
 * @author changhua.jiang
 * @since 2018/3/7 下午5:14
 */
class MyCamera(context : Context) : ICamera{

    companion object {
        val TAG = MyCamera::javaClass.name
    }
    var mStatus : ICamera.status = ICamera.status.RELEASED
    val mContext = context
    lateinit var mCamera : Camera
    lateinit var parameters : Camera.Parameters
    lateinit var mHandler : Handler
    var autoFocusCallback : Camera.AutoFocusCallback= Camera.AutoFocusCallback { success,camera ->
//        if(success){
//            mCamera.cancelAutoFocus()
//        }
//        else{
            mHandler.postDelayed(autoFocusRunnable,1000)
//        }
    }
    var autoFocusRunnable : Runnable = Runnable {
        mCamera.autoFocus(autoFocusCallback)
    }


    override fun setDisplayView(displayView: SurfaceView) {
        Log.e(TAG,"setDisplayView")
        mCamera.setPreviewDisplay(displayView.holder)
    }

    override fun open() {
        Log.e(TAG,"open")
        mCamera = Camera.open();
        if (mCamera == null)
            throw RuntimeException("open camera error")
        mHandler = Handler()
        mCamera.setDisplayOrientation(90);
        parameters = mCamera.getParameters();// 获得相机参数
        parameters.focusMode = FOCUS_MODE_AUTO
        val previewList = parameters.supportedPreviewSizes
        var size = calculateSize(previewList)
        if(size != null)
            parameters.setPreviewSize(size.width,size.height)
        val picList = parameters.supportedPictureSizes
        size = calculateSize(picList)
        if(size != null)
            parameters.setPictureSize(size.width,size.height)
        mCamera.parameters = parameters
        mStatus = ICamera.status.OPENED

    }

    override fun startPreview() {
        Log.e(TAG,"startPreview")
        mCamera.startPreview()
        mCamera.autoFocus(autoFocusCallback)
        mStatus = ICamera.status.RUNNING
    }

    override fun stopPreview() {
        Log.e(TAG,"stopPreview")
        mHandler.removeCallbacks(autoFocusRunnable)
        mCamera.stopPreview()
        mStatus = ICamera.status.OPENED
    }

    override fun release() {
        Log.e(TAG,"release")
        mCamera.setPreviewCallback(null)
        mCamera.release()
        mStatus = ICamera.status.RELEASED
    }

    override fun setPreviewCallback(callback: Camera.PreviewCallback) {
        Log.e(TAG,"setPreviewCallback")
        mCamera.setPreviewCallback(callback)
    }

    override fun getStatus(): ICamera.status {
        return mStatus
    }

    private fun calculateSize(list : List<Camera.Size>) : Camera.Size?{
        val height = ScreenUtils.getScreenHeight(mContext)
        val width = ScreenUtils.getScreenWidth(mContext)
        for(size in list){
            if(Math.abs(size.height.toFloat() / size.width.toFloat() - width.toFloat() / height.toFloat()) <= 0.1){
                return size
            }
        }
        return null
    }

}