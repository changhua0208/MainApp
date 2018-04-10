//
// Created by Administrator on 2017/5/4.
//

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include "cpu-features.h"
#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <stdio.h>
#define LOG_TAG "CPU-FEATURE"
//日志显示的等级
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jboolean JNICALL
native_isArmCpu(JNIEnv *env, jclass type) {
    AndroidCpuFamily cpuFamily = android_getCpuFamily();
    return cpuFamily == ANDROID_CPU_FAMILY_ARM;
}

JNIEXPORT jboolean JNICALL
native_isArm7Compatible(JNIEnv *env, jclass type) {
    uint64_t cpuFeatures = android_getCpuFeatures();
    return (cpuFeatures & ANDROID_CPU_ARM_FEATURE_ARMv7) == ANDROID_CPU_ARM_FEATURE_ARMv7;
}


JNIEXPORT jboolean JNICALL
native_isMipsCpu(JNIEnv *env, jclass type) {
    AndroidCpuFamily cpuFamily = android_getCpuFamily();
    return cpuFamily == ANDROID_CPU_FAMILY_MIPS;
}


JNIEXPORT jboolean JNICALL
native_isX86Cpu(JNIEnv *env, jclass type) {
    AndroidCpuFamily cpuFamily = android_getCpuFamily();
    return cpuFamily == ANDROID_CPU_FAMILY_X86;
}


JNIEXPORT jboolean JNICALL
native_isArm64Cpu(JNIEnv *env, jclass type) {
    AndroidCpuFamily cpuFamily = android_getCpuFamily();
    return cpuFamily == ANDROID_CPU_FAMILY_ARM64;
}

JNIEXPORT jboolean JNICALL
native_isMips64Cpu(JNIEnv *env, jclass type) {
    AndroidCpuFamily cpuFamily = android_getCpuFamily();
    return cpuFamily == ANDROID_CPU_FAMILY_MIPS64;
}


JNIEXPORT jboolean JNICALL
native_isX86_164Cpu(JNIEnv *env, jclass type) {
    AndroidCpuFamily cpuFamily = android_getCpuFamily();
    return cpuFamily == ANDROID_CPU_FAMILY_X86_64;
}

static char *javaClass = "com/jch/plugin/cpu/CPUFrameworkHelper";
static JNINativeMethod gMethods[] = {
        {"isArmCpu","()Z",(void *)native_isArmCpu},
        {"isArm7Compatible","()Z",(void *)native_isArm7Compatible},
        {"isMipsCpu","()Z",(void *)native_isMipsCpu},
        {"isX86Cpu","()Z",(void *)native_isX86Cpu},
        {"isArm64Cpu","()Z",(void *)native_isArm64Cpu},
        {"isMips64Cpu","()Z",(void *)native_isMips64Cpu},
        {"isX86_64Cpu","()Z",(void *)native_isX86_164Cpu}
};


int registerNativeMethods(JNIEnv *env) {
    jclass clazz = env->FindClass(javaClass);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    int len = sizeof(gMethods) / sizeof(JNINativeMethod);
    if(env->RegisterNatives(clazz, gMethods, len) < 0){
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    if (registerNativeMethods(env) != JNI_TRUE) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_4;
}


#ifdef __cplusplus
}
#endif