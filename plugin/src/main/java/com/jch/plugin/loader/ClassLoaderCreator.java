package com.jch.plugin.loader;

import android.content.Context;

import com.jch.plugin.model.ApkPluginInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author changhua.jiang
 * @since 2018/1/18 下午5:26
 */

public class ClassLoaderCreator {

    //一个插件一个classloader,插件的loader是相互隔离的,插件之间的访问是不支持的
    static Map<String,PluginClassLoader> clMap = new HashMap<>();

    public synchronized static ClassLoader create(Context context,ClassLoader parent,ApkPluginInfo info){
        if(info != null) {
            PluginClassLoader cl = loadPlugin(context,parent,info);
            return cl;
        }
        else{
            return parent;
        }
    }

    public static PluginClassLoader loadPlugin(Context context, ClassLoader parent, ApkPluginInfo info){
        PluginClassLoader cl = clMap.get(info.getApkUri());
        if(cl == null) {
            String dexPath = context.getDir("dexOutput", Context.MODE_PRIVATE).getAbsolutePath();//context.getExternalCacheDir().getAbsolutePath();//Environment.getExternalStorageDirectory().getAbsolutePath() + "/dexOutput";
            String libPath = context.getDir("libOutput", Context.MODE_PRIVATE).getAbsolutePath();
            cl = new PluginClassLoader(info.getApkUri(),dexPath,libPath,parent);
            clMap.put(info.getApkUri(),cl);
        }
        return cl;
    }

    public static boolean isClassLoaderExists(ApkPluginInfo info){
        return clMap.get(info.getApkUri()) == null ? false : true;
    }
}
