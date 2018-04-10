package com.jch.plugin.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午5:45
 */

public class AXmlAction extends AXmlElement implements Parcelable {
    private String name;

    public String getName() {
        return name;
    }

    AXmlAction(){

    }

    protected AXmlAction(Parcel in) {
        name = in.readString();
    }

    public static final Creator<AXmlAction> CREATOR = new Creator<AXmlAction>() {
        @Override
        public AXmlAction createFromParcel(Parcel in) {
            return new AXmlAction(in);
        }

        @Override
        public AXmlAction[] newArray(int size) {
            return new AXmlAction[size];
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

    public static AXmlAction resolve(XmlPullParser xmlParser) {
        AXmlAction action = new AXmlAction();
        int count = xmlParser.getAttributeCount();
        for(int i = 0;i < count;i++){
            String name = xmlParser.getAttributeName(i);
            String value = xmlParser.getAttributeValue(i);
            setField(action,name,value);
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
        return action;
    }

    public String toString(){
        String str = "<action name=\"%s\" />";
        return String.format(str,name);
    }

    public boolean equalsWith(String action){
        return name.equals(action);
    }
}
