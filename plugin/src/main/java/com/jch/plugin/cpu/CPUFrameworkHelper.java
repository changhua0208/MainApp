//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jch.plugin.cpu;

import java.util.ArrayList;
import java.util.List;

public class CPUFrameworkHelper {
    public static final String ARM64 = "arm64-v8a";
    public static final String ARMEABI_V7 = "armeabi-v7a";
    public static final String ARMEABI = "armeabi";
    public static final String MIPSABI = "mips";
    public static final String X86ABI = "x86";
    public static final String X86_64 = "x86_64";
    public static final String MIPS64 = "mips64";

    public CPUFrameworkHelper() {
    }

    public static native boolean isArmCpu();

    public static native boolean isArm7Compatible();

    public static native boolean isMipsCpu();

    public static native boolean isX86Cpu();

    public static native boolean isArm64Cpu();

    public static native boolean isMips64Cpu();

    public static native boolean isX86_64Cpu();

    static {
        System.loadLibrary("cpufeature-lib");
    }

    public static List<String> getSupportABI(){
        List<String> abiList = new ArrayList<>();
        if(isArm64Cpu()){
            abiList.add(ARM64) ;
        }
        if(isArm7Compatible()){
            abiList.add(ARMEABI_V7) ;
        }
        if(isArmCpu()){
            abiList.add(ARMEABI) ;
        }
        if(isX86Cpu()){
            abiList.add(X86ABI) ;
        }
        if(isX86_64Cpu()){
            abiList.add(X86_64) ;
        }
        if(isMipsCpu()){
            abiList.add(MIPSABI) ;
        }
        if(isMips64Cpu()){
            abiList.add(MIPS64) ;
        }
        return abiList;
    }
}
