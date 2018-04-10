package com.jch.testkot.camera

import android.hardware.Camera
import android.view.SurfaceView

/**
 *
 * @author changhua.jiang
 * @since 2018/3/7 下午5:15
 */
interface ICamera {
    fun setDisplayView(displayView : SurfaceView)
    fun open()
    fun startPreview()
    fun stopPreview()
    fun setPreviewCallback(callback: Camera.PreviewCallback)
    fun release()
    fun getStatus() : status

    enum class status{
        OPENED,RUNNING,RELEASED
    }
}