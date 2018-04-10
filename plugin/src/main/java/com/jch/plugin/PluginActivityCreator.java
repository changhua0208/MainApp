package com.jch.plugin;

import android.content.Context;

import com.jch.plugin.loader.ClassLoaderCreator;
import com.jch.plugin.model.PluginInfo;

/**
 * 从后端获得的插件信息，创建插件。
 * @author changhua.jiang
 * @since 2017/9/22 下午5:58
 */

public class PluginActivityCreator {

    public static  PluginActivity create(Context context,PluginInfo pluginInfo){
        PluginActivity plugin = null;
        try {
            Class cc = load(context,pluginInfo);
            plugin = create(context,cc,pluginInfo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plugin;
    }

    public static PluginActivity create(Context context,Class<? extends  PluginActivity> cc,PluginInfo pluginInfo){
        PluginActivity plugin = null;
        try {
            plugin = (PluginActivity) cc.newInstance();
            plugin.setContext(context);
            plugin.setPluginInfo(pluginInfo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return plugin;
    }

    public static PluginActivity create(Context context,String cn,PluginInfo pluginInfo){
        PluginActivity plugin = null;
        try {
            Class<? extends PluginActivity> cc = (Class<? extends PluginActivity>) Class.forName(cn);
            plugin = create(context,cc,pluginInfo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return plugin;
    }

    public static PluginActivity create(Context context,String cn){
        return create(context,cn,null);
    }

    public static Class<? extends PluginActivity> load(Context context,PluginInfo info) throws ClassNotFoundException {
        ClassLoader cl = ClassLoaderCreator.create(context,context.getClassLoader(),info);
        Class<? extends PluginActivity> cc = (Class<? extends PluginActivity>) cl.loadClass(info.getPackageName() + "." + info.getClassName());
        return  cc;
    }

}
