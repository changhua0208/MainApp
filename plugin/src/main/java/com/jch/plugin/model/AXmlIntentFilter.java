package com.jch.plugin.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午5:44
 */

public class AXmlIntentFilter extends AXmlElement implements Parcelable{
    private List<AXmlAction> actionList;
    private List<AXmlCategory> categoryList;
    private List<AXmlData> dataList;

    public List<AXmlAction> getActionList() {
        return actionList;
    }

    public List<AXmlCategory> getCategoryList() {
        return categoryList;
    }

    public List<AXmlData> getDataList() {
        return dataList;
    }

    AXmlIntentFilter(){

    }

    protected AXmlIntentFilter(Parcel in) {
        actionList = in.createTypedArrayList(AXmlAction.CREATOR);
        categoryList = in.createTypedArrayList(AXmlCategory.CREATOR);
        dataList = in.createTypedArrayList(AXmlData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(actionList);
        dest.writeTypedList(categoryList);
        dest.writeTypedList(dataList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AXmlIntentFilter> CREATOR = new Creator<AXmlIntentFilter>() {
        @Override
        public AXmlIntentFilter createFromParcel(Parcel in) {
            return new AXmlIntentFilter(in);
        }

        @Override
        public AXmlIntentFilter[] newArray(int size) {
            return new AXmlIntentFilter[size];
        }
    };

    void addAction(AXmlAction action){
        if(actionList == null)
            actionList = new ArrayList<>();
        actionList.add(action);
    }

    void addCategory(AXmlCategory category){
        if(categoryList == null)
            categoryList = new ArrayList<>();
        categoryList.add(category);
    }

    void addData(AXmlData data){
        if(dataList == null)
            dataList = new ArrayList<>();
        dataList.add(data);
    }

    public static AXmlIntentFilter resolve(XmlPullParser xmlParser){
        AXmlIntentFilter intentFilter = new AXmlIntentFilter();
        try {
            int event = xmlParser.next();
            while (event != XmlPullParser.END_TAG) {
                String name = xmlParser.getName();
                if (ACTION_TAG.equals(name)) {
                    intentFilter.addAction(AXmlAction.resolve(xmlParser));
                }
                if(CATEGORY_TAG.equals(name)){
                    intentFilter.addCategory(AXmlCategory.resolve(xmlParser));
                }
                if(DATA_TAG.equals(name)){
                    intentFilter.addData(AXmlData.resolve(xmlParser));
                }
                event = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return intentFilter;
    }

    @Override
    public String toString(){
        String str = "<intent-filter>\n";
        if(actionList != null){
            for(AXmlAction action : actionList){
                str += "\t";
                str += action.toString();
                str += "\n";
            }
        }
        if(categoryList != null){
            for(AXmlCategory category : categoryList){
                str += "\t";
                str += category.toString();
                str += "\n";
            }
        }

        if(dataList != null){
            for(AXmlData data : dataList){
                str += "\t";
                str += data.toString();
                str += "\n";
            }
        }
        str += "</intent-filter>";
        return str;
    }

    public boolean resolveIntent(Intent intent) {
        String action = intent.getAction();
        if(TextUtils.isEmpty(action)){
            return false;
        }
        if(actionList == null)
            return false;
        for(AXmlAction xmlAction : actionList){
            if(xmlAction.equalsWith(action)){
                Uri uri = intent.getData();
                if(uri != null){
                    if(dataList == null)
                        return false;
                    for(AXmlData data : dataList){
                        if(data.equalsWith(uri)){
                            return true;
                        }
                    }
                }
                else{
                    if(categoryList == null)
                        return false;
                    for(AXmlCategory category : categoryList){
                        if(!intent.hasCategory(category.getName())){
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
