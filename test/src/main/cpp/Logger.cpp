#include "Logger.h"
#include <android/log.h>
#include <stdlib.h>

//修改日志tag中的值
#define LOG_TAG "Logger"
//日志显示的等级
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define classPathName "com/jch/test/Logger"
#define nativeMethodError native_error
#define javaMethodError  "e"
#define signatureMethodError "(Ljava/lang/String;Ljava/lang/String;)V"

// Java和JNI函数的绑定表
static JNINativeMethod method_table[] = {
        {javaMethodError, signatureMethodError, (void *) nativeMethodError}//绑定
};

// 注册native方法到java中
static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *methods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return FALSE;
    }
    if (env->RegisterNatives(clazz, methods, numMethods) < 0) {
        return FALSE;
    }

    return TRUE;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    if (registerNativeMethods(env, classPathName, method_table, ARR_LEN(method_table)) !=
        TRUE) {
        LOGE("ERROR: registerNatives failed");
        return -1;
    }

    return JNI_VERSION_1_4;
}


/*
 * Class:     com_jch_test_Logger
 * Method:    e
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL native_error
        (JNIEnv *env, jclass cls, jstring tag, jstring msg) {
    const char *cTag = (char *) env->GetStringUTFChars(tag, 0);
    const char *cMsg = (char *) env->GetStringUTFChars(msg, 0);
    __android_log_print(ANDROID_LOG_ERROR,cTag,cMsg);
}