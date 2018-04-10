package com.jch.plugin.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午6:00
 */

public class AXmlCategory extends AXmlElement implements Parcelable{
    private String name;

    public String getName() {
        return name;
    }

    AXmlCategory(){

    }

    protected AXmlCategory(Parcel in) {
        name = in.readString();
    }

    public static final Creator<AXmlCategory> CREATOR = new Creator<AXmlCategory>() {
        @Override
        public AXmlCategory createFromParcel(Parcel in) {
            return new AXmlCategory(in);
        }

        @Override
        public AXmlCategory[] newArray(int size) {
            return new AXmlCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static AXmlCategory resolve(XmlPullParser xmlParser) {
        AXmlCategory category = new AXmlCategory();
        int count = xmlParser.getAttributeCount();
        for(int i = 0;i < count;i++){
            String name = xmlParser.getAttributeName(i);
            String value = xmlParser.getAttributeValue(i);
            setField(category,name,value);
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
        return category;
    }

    public String toString(){
        String str = "<category name=\"%s\" />";
        return String.format(str,name);
    }
}
