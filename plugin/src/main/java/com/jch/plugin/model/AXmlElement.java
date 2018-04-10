package com.jch.plugin.model;

import com.jch.plugin.clazz.ReflectUtils;

/**
 * @author changhua.jiang
 * @since 2018/2/1 下午6:02
 */

public abstract class AXmlElement {

    public static final String ACTIVITY_TAG = "activity";
    public static final String INTENT_FILTER_TAG = "intent-filter";
    public static final String ACTION_TAG = "action";
    public static final String CATEGORY_TAG = "category";
    public static final String META_DATA_TAG = "meta-data";
    public static final String DATA_TAG = "data";


    public static void setField(AXmlElement element,String attrName,String value){
        try {
            ReflectUtils.setFieldValue(element.getClass(), element, attrName, value);
        }
        catch (Exception e){

        }
    }

}
