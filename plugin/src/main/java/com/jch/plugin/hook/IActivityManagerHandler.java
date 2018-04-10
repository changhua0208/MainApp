package com.jch.plugin.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jch.plugin.ProxyActivity;
import com.jch.plugin.model.PluginInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 原作者名字，保留
 * @author 刘镓旗
 * @date 17/2/21
 */
class IActivityManagerHandler implements InvocationHandler {

    private static final String TAG = "IActivityManagerHandler";

    private Object mBase;
    private Context mContext;

    public IActivityManagerHandler(Context context,Object base) {
        mBase = base;
        mContext = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("startActivity".equals(method.getName())) {
            Log.e(TAG, "劫持了startActivity");
            // 找到参数里面的第一个Intent 对象
            Intent raw;
            int index = 0;

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            raw = (Intent) args[index];
            PluginInfo info = raw.getParcelableExtra("PLUGIN");
            if(info != null) {
                //创建一个要被掉包的Intent
                Intent newIntent = new Intent();
                // 替身Activity的包名, 也就是我们自己的"包名"
                String stubPackage = mContext.getPackageName();
                // 这里我们把启动的Activity临时替换为 ProxyActivity
                ComponentName componentName = new ComponentName(stubPackage, ProxyActivity.class.getName());
                newIntent.setComponent(componentName);
                // 把我们原始要启动的TargetActivity先存起来
                newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT, raw);
                // 替换掉Intent, 达到欺骗AMS的目的
                args[index] = newIntent;
                return method.invoke(mBase, args);
            }
        }

        return method.invoke(mBase, args);
    }
}
