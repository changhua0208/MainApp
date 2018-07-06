package com.jch.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.jch.plugin.ShellActivity
import com.jch.plugin.model.PluginInfo
import com.jch.utils.FileHelper
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException

/**
 *
 * @author changhua.jiang
 * @since 2018/2/27 下午3:30
 */
class KotMainActivity : ShellActivity() {
    val mPluginInfos : MutableList<PluginInfo>  = ArrayList()
    val mAdapter = PluginListAdapter(this)
    //var progressDlg : ProgressDialog? = null
    var mPermission = false

    companion object {
        val MSG_ERROR = 400
        val MSG_SUCCESS = 500
        val REQUST_PERMISSION = 100
        val URL = "http://139.199.39.133/test/"
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MSG_ERROR -> Toast.makeText(this@KotMainActivity,"请求失败~",Toast.LENGTH_SHORT).show()
                MSG_SUCCESS -> {
                    val strJson = msg.data.getString("PLUGINS")
                    val array  = JSON.parseArray(strJson, HashMap::class.java)
                    for(map in array){

                        val apkName = map.get("name") as String
                        val className = map.get("launcher") as String
                        val icon = map.get("icon") as String
                        val packageName = map.get("packageName") as String
                        val apkPath = map.get("apkPath") as String
                        val parent = this.javaClass.name
                        val plugin = PluginInfo.Builder(pluginRootPath)
                                .setApkName(apkName)
                                .setClassName(className)
                                .setIcon(icon)
                                .setPackageName(packageName)
                                .setApkPath(apkPath)
                                .setParent(parent)
                                .create();
                        mPluginInfos.add(plugin)
                    }

                    if(array != null && mPermission){
                        preparePlugins()
                        //AXmlHolder.init()
                        mAdapter.addPlugins(mPluginInfos)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //progressDlg = ProgressDialog(this)
        rv_plugin_list.layoutManager = LinearLayoutManager(this)
        rv_plugin_list.adapter = mAdapter
        checkPermissions()
        sendRequest()
    }

    fun sendRequest(){
        val client = OkHttpClient()
        val testMap = JSONObject()
        testMap.put("client_type","android")
        val json = testMap.toJSONString()
        val body = RequestBody.create(MediaType.parse("application/json"),json)
        val request = Request.Builder()
                .url(URL + "api/plugins/getAll.do")
                .post(body)
                .build()
        request.body()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                handler.sendEmptyMessage(MSG_ERROR)
            }

            override fun onResponse(call: Call?, response: Response) {
                if(response.isSuccessful && response.code() == 200){
                    val msg = Message.obtain()
                    msg.what = MSG_SUCCESS
                    val data = Bundle()
                    data.putString("PLUGINS",response.body().string())
                    msg.data = data
                    handler.sendMessage(msg)
                }
                else{
                    handler.sendEmptyMessage(MSG_ERROR)
                }
            }

        })
    }

    //向服务器请求插件信息
    fun checkPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if(!checkPermissionAllGranted(permissions)){
                ActivityCompat.requestPermissions(this,permissions,REQUST_PERMISSION)
            }
            else{
                mPermission = true
            }
        }
        else{
            mPermission = true

        }
    }

    fun checkPermissionAllGranted(permissions: Array<String>) : Boolean{
        for(p in permissions){
            if(ContextCompat.checkSelfPermission(this,p) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUST_PERMISSION ){
            mPermission = true
            if(!mPluginInfos.isEmpty()){
                preparePlugins()
            }
        }
    }

    @Deprecated("use plugins from network")
    fun loadPlugins() {
        val xmlParser = resources.getXml(R.xml.plugin)

        try {
            var event = xmlParser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_DOCUMENT -> {
                    }
                    XmlPullParser.START_TAG ->
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        if (xmlParser.name == "Plugin") {
                            //两种方法获取属性值
                            val name = xmlParser.getAttributeValue(null, "name")
                            val launcher = xmlParser.getAttributeValue(null, "launcher")
                            val apkPath = xmlParser.getAttributeValue(null, "apkPath")
                            val nativeLib = xmlParser.getAttributeValue(null, "nativeLib")
                            val icon = xmlParser.getAttributeValue(null, "icon")
                            val packageName = xmlParser.getAttributeValue(null, "packageName")
                            val pluginInfo = PluginInfo.Builder(pluginRootPath)
                                    .setApkName(name)
                                    .setApkPath(apkPath)
                                    .setIcon(icon)
                                    .setPackageName(packageName)
                                    .setClassName(launcher)
                                    .setParent(this.javaClass.name)
                                    .create()

                            mPluginInfos.add(pluginInfo)
                        }
                    XmlPullParser.END_TAG -> {
                    }
                    else -> {
                    }
                }
                event = xmlParser.next()   //将当前解析器光标往下一步移
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun preparePlugins(){
        for(plugin in mPluginInfos){
            createDirifNotExists(plugin.apkPath)
            val input = assets.open(plugin.apkName)
            FileHelper.copyFileFrom(input,plugin.apkUri)
            input.close()
        }
    }

    fun createDirifNotExists(path : String){
        var dir = File(path)
        if(!dir.exists())
            dir.mkdir()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tv : TextView = itemView?.findViewById(R.id.plugin_name) as TextView
        val img : ImageView = itemView?.findViewById(R.id.plugin_icon) as ImageView
    }

    inner class PluginListAdapter(context: Context) : Adapter<ViewHolder>(){
        private var mContext : Context = context
        private var list : MutableList<PluginInfo> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            var itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_item_plugin,null)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val info = this.list[position]
            holder?.tv?.text = info.apkUri
            holder?.img?.setImageResource(getIcon(info.icon))
            holder?.itemView?.setOnClickListener {
                if (this@KotMainActivity.mPermission) {
                    loadApp(info)
                }
            }
        }

        override fun getItemCount(): Int {
            return this.list.size
        }

        fun addPlugins(list: List<PluginInfo>) {
            this.list.addAll(list)
        }

        fun getIcon(icon : String) : Int{
            val iconInfo = icon.split("/")
            val pkName = javaClass.`package`.name
            val clazzName = pkName + ".R$" + iconInfo[0]
            var resId = -1
            try {
                val clazz = Class.forName(clazzName)
                val field = clazz.getField(iconInfo[1])
                resId = field.getInt(field.name)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return resId
        }
    }

}
