package com.jch.plugin.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午5:45
 */

public class AXmlData extends AXmlElement implements Parcelable{
    private String scheme;
    private String host;
    private String port = "-1";
    private String path;
    private String pathPrefix;
    private String pathPattern;
    private String mimeType;
    private String ssp;
    private String sspPattern;
    private String sspPrefix;

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getSsp() {
        return ssp;
    }

    public String getSspPattern() {
        return sspPattern;
    }

    public String getSspPrefix() {
        return sspPrefix;
    }

    AXmlData(){

    }

    protected AXmlData(Parcel in) {
        scheme = in.readString();
        host = in.readString();
        port = in.readString();
        path = in.readString();
        pathPrefix = in.readString();
        pathPattern = in.readString();
        mimeType = in.readString();
        ssp = in.readString();
        sspPattern = in.readString();
        sspPrefix = in.readString();
    }

    public static final Creator<AXmlData> CREATOR = new Creator<AXmlData>() {
        @Override
        public AXmlData createFromParcel(Parcel in) {
            return new AXmlData(in);
        }

        @Override
        public AXmlData[] newArray(int size) {
            return new AXmlData[size];
        }
    };

    public static AXmlData resolve(XmlPullParser xmlParser){
        AXmlData data = new AXmlData();
        int count = xmlParser.getAttributeCount();
        for(int i = 0;i < count;i++){
            String name = xmlParser.getAttributeName(i);
            String value = xmlParser.getAttributeValue(i);
            setField(data,name,value);
        }
        try {
            int event = xmlParser.next();
            while (event != XmlPullParser.END_TAG) {
                event = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scheme);
        dest.writeString(host);
        dest.writeString(port);
        dest.writeString(path);
        dest.writeString(pathPrefix);
        dest.writeString(pathPattern);
        dest.writeString(mimeType);
        dest.writeString(ssp);
        dest.writeString(sspPattern);
        dest.writeString(sspPrefix);
    }

    public String toString(){
        //懒得弄，太多
        String str = "<data scheme=\"" + scheme + "\" host=\"" + host + "\" />";
        return str;
    }

    //目前只解析了scheme://host:port/path 其余的暂不解析
    public boolean equalsWith(Uri uri){
        if(!uri.getScheme().equals(scheme))
            return false;
        if(!uri.getHost().equals(host)){
            return false;
        }
        if(Integer.valueOf(port) != uri.getPort()){
            return false;
        }
        if(TextUtils.isEmpty(uri.getPath())){
            if(path != null || pathPattern != null || pathPrefix != null){
                return false;
            }
        }
        else{
            if(path == null && pathPattern == null && pathPrefix == null){
                return false;
            }
            if(path != null && !TextUtils.equals(path,uri.getPath())){
                return false;
            }
            if(pathPattern != null && !uri.getPath().matches(pathPattern)){
                return false;
            }
            if(pathPrefix != null && !uri.getPath().startsWith(pathPrefix)){
                return false;
            }
        }
        return true;
    }
}
