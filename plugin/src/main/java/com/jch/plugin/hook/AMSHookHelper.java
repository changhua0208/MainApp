package com.jch.plugin.hook;

import android.app.Application;
import android.os.Build;
import android.os.Handler;

import com.jch.plugin.clazz.ReflectUtils;

import java.lang.reflect.Proxy;

public class AMSHookHelper {

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";
    public static int LAUNCH_ACTIVITY;
    public static Application mApplication;

    public static void init(Application application){
        if(mApplication == null)
            mApplication = application;
        else{
            throw new RuntimeException("AMSHookHelper is initialized already ");
        }
    }
    /**
     * Hook AMS
     * <p/>
     * 主要完成的操作是  "把真正要启动的Activity临时替换为在AndroidManifest.xml中声明的替身Activity"
     * <p/>
     * 进而骗过AMS
     *
     */
    public static void hookActivityManagerNative() {
        if(Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 26) {
            try {
                //从gDefault字段中取出这个对象的值
                Object gDefault = ReflectUtils.getFieldValue("android.app.ActivityManagerNative", null, "gDefault");
                //ams的代理对象
                Object rawIActivityManager = ReflectUtils.getFieldValue("android.util.Singleton", gDefault, "mInstance");//mInstanceField.get(gDefault);

                // 创建一个这个对象的代理对象, 然后替换这个字段, 让我们的代理对象帮忙干活,这里我们使用动态代理
                Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
                Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{iActivityManagerInterface}, new IActivityManagerHandler(mApplication, rawIActivityManager));
                ReflectUtils.setFieldValue("android.util.Singleton", gDefault, "mInstance", proxy);
            } catch (Exception e) {
                new RuntimeException("hookActivityManagerNative failed,create proxy error");
            }
        }
        else if(Build.VERSION.SDK_INT < 16){
            try {
                Object gDefault = ReflectUtils.getFieldValue("android.app.ActivityManagerNative", null, "gDefault");
                Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
                Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{iActivityManagerInterface}, new IActivityManagerHandler(mApplication, gDefault));
                ReflectUtils.setFieldValue("android.app.ActivityManagerNative",null,"gDefault",proxy);
            }
            catch (Exception e){
                new RuntimeException("hookActivityManagerNative failed,create proxy error");
            }
        }
        else{// >26
            try {
                Object IActivityManagerSingleton = ReflectUtils.getFieldValue("android.app.ActivityManager", null, "IActivityManagerSingleton");
                //ams的代理对象
                Object rawIActivityManager = ReflectUtils.getFieldValue("android.util.Singleton", IActivityManagerSingleton, "mInstance");
                // 创建一个这个对象的代理对象, 然后替换这个字段, 让我们的代理对象帮忙干活,这里我们使用动态代理
                Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
                Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{iActivityManagerInterface}, new IActivityManagerHandler(mApplication, rawIActivityManager));
                ReflectUtils.setFieldValue("android.util.Singleton", IActivityManagerSingleton, "mInstance", proxy);
            }
            catch (Exception e){
                new RuntimeException("hookActivityManagerNative failed,create proxy error");
            }
        }

    }

    /**
     * 由于之前我们用替身欺骗了AMS; 现在我们要换回我们真正需要启动的Activity
     * <p/>
     * 不然就真的启动替身了, 狸猫换太子...
     * <p/>
     * 到最终要启动Activity的时候,会交给ActivityThread 的一个内部类叫做 H 来完成
     * H 会完成这个消息转发; 最终调用它的callback
     */
    public static void hookActivityThreadHandler() {

        Object currentActivityThread = ReflectUtils.invokeMethod("android.app.ActivityThread",null,"currentActivityThread",null,null);//currentActivityThreadMethod.invoke(null);
        //得到H这个Handler
        Handler mH = (Handler) ReflectUtils.getFieldValue("android.app.ActivityThread",currentActivityThread,"mH");//(Handler) mHField.get(currentActivityThread);
        ReflectUtils.setFieldValue(Handler.class,mH,"mCallback",new ActivityThreadHandlerCallback(mH));

    }

}
