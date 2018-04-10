package com.jch.plugin.model;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午5:39
 */

public class AXmlActivityInfo extends AXmlElement implements Parcelable {

    private String name;
    private List<AXmlIntentFilter> filterList;
    private List<AXmlMetaData> metaDataList;

    public String getName() {
        return name;
    }

    public List<AXmlIntentFilter> getFilterList() {
        return filterList;
    }

    public List<AXmlMetaData> getMetaDataList() {
        return metaDataList;
    }

    AXmlActivityInfo() {
//        filterList = new ArrayList<>();
//        dataList = new ArrayList<>();
//        metaDataList = new ArrayList<>();
    }

    protected AXmlActivityInfo(Parcel in) {
        name = in.readString();
        filterList = in.createTypedArrayList(AXmlIntentFilter.CREATOR);
        metaDataList = in.createTypedArrayList(AXmlMetaData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(filterList);
        dest.writeTypedList(metaDataList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AXmlActivityInfo> CREATOR = new Creator<AXmlActivityInfo>() {
        @Override
        public AXmlActivityInfo createFromParcel(Parcel in) {
            return new AXmlActivityInfo(in);
        }

        @Override
        public AXmlActivityInfo[] newArray(int size) {
            return new AXmlActivityInfo[size];
        }
    };

    void addFilter(AXmlIntentFilter filter) {
        if (filterList == null)
            filterList = new ArrayList<>();
        filterList.add(filter);
    }

    void addMetaData(AXmlMetaData data) {
        if (metaDataList == null)
            metaDataList = new ArrayList<>();
        metaDataList.add(data);
    }

    public static AXmlActivityInfo resolve(XmlPullParser xmlParser) {
        AXmlActivityInfo activityInfo = new AXmlActivityInfo();
        int count = xmlParser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String name = xmlParser.getAttributeName(i);
            String value = xmlParser.getAttributeValue(i);
            setField(activityInfo, name, value);
        }
        try {
            int event = xmlParser.next();
            while (event != XmlPullParser.END_TAG) {
                String name = xmlParser.getName();
                if (INTENT_FILTER_TAG.equals(name)) {
                    AXmlIntentFilter filter = AXmlIntentFilter.resolve(xmlParser);
                    activityInfo.addFilter(filter);
                }
                if (META_DATA_TAG.equals(name)) {
                    AXmlMetaData data = AXmlMetaData.resolve(xmlParser);
                    activityInfo.addMetaData(data);
                }
                event = xmlParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activityInfo;
    }

    public String toString() {
        String str = "<activity name=\"%s\">\n";
        str = String.format(str, name);
        if (filterList != null) {
            for (AXmlIntentFilter filter : filterList) {
                str += "\t";
                str += filter.toString();
                str += "\n";
            }
        }
        if (metaDataList != null) {
            for (AXmlMetaData data : metaDataList) {
                str += "\t";
                str += data.toString();
                str += "\n";
            }
        }
        str += "</activity>";
        return str;
    }

    public boolean resolveIntent(Intent intent){
        if(filterList == null){
            return false;
        }
        for (AXmlIntentFilter filter : filterList) {
            if (filter.resolveIntent(intent)) {
                int index = name.lastIndexOf(".");
                if(index > 0) {
                    String packageName = name.substring(0, index);
                    ComponentName componentName = new ComponentName(packageName, name);
                    intent.setComponent(componentName);
                }
                return true;
            }
        }
        return false;
    }
}
