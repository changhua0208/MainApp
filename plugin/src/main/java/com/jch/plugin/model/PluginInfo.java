package com.jch.plugin.model;

import android.os.Parcel;


/**
 * @author changhua.jiang
 * @since 2017/9/22 下午6:00
 */

public class PluginInfo extends ApkPluginInfo implements Cloneable{
    //插件报名
    private String packageName;
    //上一个发起页
    private String parent;
    //被发起页面className
    private String className;


    protected PluginInfo(Parcel in) {
        super(in);
        packageName = in.readString();
        parent = in.readString();
        className = in.readString();
    }

    protected PluginInfo(){

    }

    public PluginInfo(Builder builder){
        this.apkName = builder.apkName;
        this.apkPath = builder.apkPath;
        this.icon = builder.icon;
        this.packageName = builder.packageName;
        this.parent = builder.parent;
        this.className = builder.className;
    }

    public static final Creator<PluginInfo> CREATOR = new Creator<PluginInfo>() {
        @Override
        public PluginInfo createFromParcel(Parcel in) {
            return new PluginInfo(in);
        }

        @Override
        public PluginInfo[] newArray(int size) {
            return new PluginInfo[size];
        }
    };

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(packageName);
        dest.writeString(parent);
        dest.writeString(className);
    }

    public PluginInfo clone(){
        PluginInfo pluginInfo = new PluginInfo();
        pluginInfo.setPackageName(packageName);
        pluginInfo.setClassName(className);
        pluginInfo.setParent(parent);
        pluginInfo.setIcon(icon);
        pluginInfo.setApkPath(apkPath);
        pluginInfo.setApkName(apkName);
        return pluginInfo;
    }

    public static class Builder{

        private String rootDir;
        private String apkName;
        private String apkPath;
        private String icon;
        private String packageName;
        private String parent;
        private String className;

        public Builder(String rootDir){
            this.rootDir = rootDir;
        }

        public Builder setApkName(String apkName) {
            this.apkName = apkName;
            return this;
        }

        public Builder setApkPath(String apkPath) {
            this.apkPath = rootDir + "/" + apkPath;
            return this;
        }

        public Builder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder setParent(String parent) {
            this.parent = parent;
            return this;
        }

        public Builder setClassName(String className) {
            this.className = className;
            return this;
        }

        public PluginInfo create(){
            PluginInfo pluginInfo = new PluginInfo(this);
            return pluginInfo;
        }
    }
}
