package com.jch.plugin.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午5:48
 */

public class AXmlMetaData extends AXmlElement implements Parcelable{
    private String name;
    private String value;
    private String resource;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getResource() {
        return resource;
    }

    AXmlMetaData(){

    }

    protected AXmlMetaData(Parcel in) {
        name = in.readString();
        value = in.readString();
        resource = in.readString();
    }

    public static final Creator<AXmlMetaData> CREATOR = new Creator<AXmlMetaData>() {
        @Override
        public AXmlMetaData createFromParcel(Parcel in) {
            return new AXmlMetaData(in);
        }

        @Override
        public AXmlMetaData[] newArray(int size) {
            return new AXmlMetaData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
        dest.writeString(resource);
    }

    public static AXmlMetaData resolve(XmlPullParser xmlParser){
        AXmlMetaData metaData = new AXmlMetaData();
        int count = xmlParser.getAttributeCount();
        for(int i = 0;i < count;i++){
            String name = xmlParser.getAttributeName(i);
            String value = xmlParser.getAttributeValue(i);
            setField(metaData,name,value);
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
        return metaData;
    }

    public String toString(){
        String str = "<meta-data name=\"%s\"";
        str = String.format(str,name);
        if(!TextUtils.isEmpty(value)){
            str += (" value=\"" + value + "\"");
        }
        if(!TextUtils.isEmpty(resource)){
            str += (" resource=\"" + resource + "\"");
        }
        str += "/>";
        return str;
    }
}
