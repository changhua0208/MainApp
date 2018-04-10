package com.jch.test;

/**
 * @author changhua.jiang
 * @since 2018/1/23 下午2:45
 */

public class Logger {

    static {
        System.loadLibrary("logger");
    }

    public static native void e(String tag,String msg);
}
