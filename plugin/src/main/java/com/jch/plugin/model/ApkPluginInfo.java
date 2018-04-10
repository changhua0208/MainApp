package com.jch.plugin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author changhua.jiang
 * @since 2018/1/17 下午3:28
 */

public class ApkPluginInfo implements Parcelable {
    //插件apk名称
    protected String apkName;
    //插件apk路径
    protected String apkPath;
    //插件在宿主程序中的icon
    protected String icon;

    public ApkPluginInfo(){

    }

    public ApkPluginInfo(Parcel in) {
        apkName = in.readString();
        apkPath = in.readString();
        icon = in.readString();
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static final Creator<ApkPluginInfo> CREATOR = new Creator<ApkPluginInfo>() {
        @Override
        public ApkPluginInfo createFromParcel(Parcel in) {
            return new ApkPluginInfo(in);
        }

        @Override
        public ApkPluginInfo[] newArray(int size) {
            return new ApkPluginInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apkName);
        dest.writeString(apkPath);
        dest.writeString(icon);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof ApkPluginInfo){
            ApkPluginInfo info = (ApkPluginInfo) obj;
            if(apkName.equalsIgnoreCase(info.getApkName()) && apkPath.equalsIgnoreCase(info.getApkPath())){
                return true;
            }

        }
        return false;
    }

    public String getApkUri(){
        return this.apkPath + "/" + apkName + ".apk";
    }

}
