package com.jch.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jch.plugin.ShellActivity;
import com.jch.plugin.axml.AXmlHolder;
import com.jch.plugin.model.PluginInfo;
import com.jch.utils.FileHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ShellActivity {

    private boolean accessable = false;
    private static final int PERMISSION_CODE = 100;
    private List<PluginInfo> pluginInfos = new ArrayList<>();
    private RecyclerView listView;
    private PluginListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = (RecyclerView) findViewById(R.id.rv_plugin_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PluginListAdapter();
        listView.setAdapter(adapter);

        //dalvik ，2.2开始，采用jit策略，优化dvm，4.4 dvm与art共存 5.0 只余下采用aot策略的art
        //isVmArt();

        checkPermission();
    }

    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkPermissionAllGranted(permissions)) {
                accessable = true;
                loadPlugins();
                adapter.notifyDataSetChanged();
                for (PluginInfo info : pluginInfos) {
                    preparePlugin(info);
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
            }
        }
        else{
            accessable = true;
            loadPlugins();
            adapter.notifyDataSetChanged();
            for (PluginInfo info : pluginInfos) {
                preparePlugin(info);
                AXmlHolder.init(info);
            }
        }
    }

    public void onLoadApp(PluginInfo info ){
        if(accessable) {
            loadApp(info);
        }
        else{
            showMsg("权限问题！！");
        }

    }

//    private boolean isVmArt(){
//        String vmVersion = System.getProperty("java.vm.version");
//        return (Build.VERSION.SDK_INT >= 21 || isVmArt(vmVersion));
//    }
//
//    private boolean isVmArt(String versionString) {
//        boolean isArt = false;
//        if (versionString != null) {
//            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
//            if (matcher.matches()) {
//                try {
//                    int major = Integer.parseInt(matcher.group(1));
//                    int minor = Integer.parseInt(matcher.group(2));
//                    isArt = (major > 2)
//                            || ((major == 2)
//                            && (minor >= 1));
//                } catch (NumberFormatException e) {
//                    // let isMultidexCapable be false
//                }
//            }
//        }
//        return isArt;
//    }

    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            accessable = true;
            loadPlugins();
            adapter.notifyDataSetChanged();
            for(PluginInfo info : pluginInfos) {
                preparePlugin(info);
                AXmlHolder.init(info);
            }
        }
    }

    private File createDirIfNotExists(String name){
        File dir  = new File(name);//this.getApplicationContext().getDir(name, Context.MODE_PRIVATE);
        if(!dir.exists()){
            dir.mkdir();
        }
        else{
            if(!dir.isDirectory()){
                throw new RuntimeException(String.format("unexpected file '%s'",name));
            }
        }
        return dir;
    }

    private void preparePlugin(PluginInfo info){

        File apkDir = createDirIfNotExists(info.getApkPath());
        //File dexOutput = createDirIfNotExists("dexOutput");
        File file = new File(info.getApkUri());

        if(file.exists()){
            file.delete();
        }
        AssetManager mgr = getApplicationContext().getAssets();
        try {
            InputStream in= mgr.open(info.getApkName());
            FileHelper.copyFileFrom(in,file);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadPlugins(){
        XmlResourceParser xmlParser = getResources().getXml(R.xml.plugin);

        try {
            int event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        if (xmlParser.getName().equals("Plugin")){
                            //两种方法获取属性值
                            String name = xmlParser.getAttributeValue(null,"name");
                            String launcher = xmlParser.getAttributeValue(null,"launcher");
                            String apkPath = xmlParser.getAttributeValue(null,"apkPath");
                            String nativeLib = xmlParser.getAttributeValue(null,"nativeLib");
                            String icon = xmlParser.getAttributeValue(null,"icon");
                            String packageName = xmlParser.getAttributeValue(null,"packageName");
                            PluginInfo pluginInfo = new PluginInfo.Builder(getPluginRootPath())
                                    .setApkName(name)
                                    .setApkPath(apkPath)
                                    .setClassName(launcher)
                                    .setIcon(icon)
                                    .setPackageName(packageName)
                                    .setParent(this.getClass().getName())
                                    .create();
                            pluginInfos.add(pluginInfo);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();   //将当前解析器光标往下一步移
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    class PluginViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv;
        public PluginViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.plugin_icon);
            tv = (TextView) itemView.findViewById(R.id.plugin_name);
        }
    }

    class PluginListAdapter extends RecyclerView.Adapter<PluginViewHolder>{

        @Override
        public PluginViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.rv_item_plugin,null);
            return new PluginViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PluginViewHolder holder, int position) {
            final PluginInfo pluginInfo = pluginInfos.get(position);
            holder.tv.setText(pluginInfo.getApkUri());
            String icon = pluginInfo.getIcon();
            int resId = getIcon(icon);
            if(resId > 0){
                holder.img.setImageResource(resId);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadApp(pluginInfo);
                }
            });

            //holder.img.setIm
        }

        @Override
        public int getItemCount() {
            return pluginInfos.size();
        }

        private int getIcon(String icon){
            String iconInfo[] = icon.split("/");
            String drawableClassName = this.getClass().getPackage().getName() + ".R$" + iconInfo[0];
            int resId = -1;
            try {
                Class clazz = Class.forName(drawableClassName);
                Field field = clazz.getField(iconInfo[1]);
                resId = field.getInt(field.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resId;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
