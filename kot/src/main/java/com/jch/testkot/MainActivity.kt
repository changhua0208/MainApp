package com.jch.testkot

import android.Manifest
import android.hardware.Camera
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.jch.plugin.PluginActivity
import com.jch.test.R
import com.jch.testkot.camera.ICamera
import com.jch.testkot.camera.MyCamera

class MainActivity : PluginActivity(), SurfaceHolder.Callback, Camera.PreviewCallback,View.OnClickListener {
    val TAG: String = "MainActivity"
    val REQUEST_CODE = 100
    lateinit var mSurfaceHolder: SurfaceHolder
    lateinit var mSurface : SurfaceView
    lateinit var mCamera : ICamera
    lateinit var mShot : ImageView
    lateinit var mLight : ImageView
    lateinit var mSaveImage : ImageView
    lateinit var mCancelSave : ImageView
    lateinit var mEditPanel : View
    lateinit var mSrcImage : ImageView
    lateinit var mSmoothImage : ImageView
    lateinit var mBinImage : ImageView
    lateinit var mBtnBegin: Button
    var take_photo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
    }

    private fun setUpViews(){
        mSurface = findViewById(R.id.sv_preview) as SurfaceView
        mShot = findViewById(R.id.icon_shot) as ImageView
        mLight = findViewById(R.id.icon_light) as ImageView
        mSaveImage = findViewById(R.id.icon_ok) as ImageView
        mCancelSave = findViewById(R.id.icon_cancel) as ImageView
        mEditPanel = findViewById(R.id.edit_panel)
        mSrcImage = findViewById(R.id.src_img) as ImageView
        mSmoothImage = findViewById(R.id.s_img) as ImageView
        mBinImage = findViewById(R.id.b_img) as ImageView
        mBtnBegin = findViewById(R.id.btn_begin) as Button

        mShot.setOnClickListener(this)
        mSaveImage.setOnClickListener(this)
        mCancelSave.setOnClickListener(this)
        mLight.setOnClickListener(this)
        mBtnBegin.setOnClickListener(this)

        mSurfaceHolder = mSurface.holder
        mSurfaceHolder.addCallback(this)
        mSurfaceHolder.setKeepScreenOn(true);

    }

    override fun onDestroy() {
        super.onDestroy()
        when(mCamera.getStatus()){
            ICamera.status.OPENED -> mCamera.release()
            ICamera.status.RUNNING -> {mCamera.stopPreview();mCamera.release()}
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.icon_shot -> {
                take_photo = true
                mCancelSave.visibility = View.VISIBLE
                mSaveImage.visibility = View.VISIBLE
                mLight.visibility = View.GONE
                //mCamera.stopPreview()
            }
            R.id.icon_cancel ->{
                take_photo = false
                mCamera.startPreview()
                mCancelSave.visibility = View.GONE
                mSaveImage.visibility = View.GONE
                mLight.visibility = View.VISIBLE
            }
            R.id.icon_ok ->{
                mEditPanel.visibility = View.VISIBLE
                mCancelSave.visibility = View.GONE
                mSaveImage.visibility = View.GONE
                mLight.visibility = View.GONE
                mCamera.release()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.size > 0) {
                Log.e(TAG,"onRequestPermissionsResult")
                mCamera.open()
                mCamera.setDisplayView(mSurface)
                mCamera.setPreviewCallback(this)
                mCamera.startPreview()
            }
            else{
                System.exit(0)
            }
        }
    }
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        //mCamera.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.e(TAG,"surfaceDestroyed")
        //mCamera.stopPreview()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mCamera = MyCamera(this@MainActivity.currentActivity)
        if(checkPermission()){
            Log.e(TAG,"surfaceCreated")
            mCamera.open()
            mCamera.setDisplayView(mSurface)
            mCamera.setPreviewCallback(this)
            mCamera.startPreview()
        }
        else{
            val arr = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(currentActivity,arr,REQUEST_CODE)
        }

    }

    private fun checkPermission(): Boolean{
        return ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        Log.e(TAG,"onPreviewFrame")
        if(take_photo ){
            //Log.e(TAG,"onPreviewFrame")
            mCamera.stopPreview()
        }

    }
}
