package com.jch.plugin.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.jch.plugin.clazz.ReflectUtils;

public class ActivityThreadHandlerCallback implements Handler.Callback {

    private static final String TAG = "ActivityThread";
    Handler mBase;

    public ActivityThreadHandlerCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
        }

        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        // 这里简单起见,直接取出TargetActivity;
        Object obj = msg.obj;
        try {
            // 把替身恢复成真身
            Intent raw = (Intent) ReflectUtils.getFieldValue(obj.getClass(),obj,"intent");

            Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
            ReflectUtils.setFieldValue(obj,"intent",target);
            //Log.e(TAG,"target is " + target.toString());

        } catch (Exception e) {
            throw new RuntimeException("hook launch activity failed", e);
        }
    }

}
