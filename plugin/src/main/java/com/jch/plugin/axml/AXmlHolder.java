package com.jch.plugin.axml;

import android.content.Intent;

import com.jch.plugin.model.AXmlActivityInfo;
import com.jch.plugin.model.AXmlElement;
import com.jch.plugin.model.ApkPluginInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 保存了apk的manifest中的activity信息，用于Intent隐式调用
 * @author changhua.jiang
 * @since 2018/2/5 下午3:46
 */

public class AXmlHolder {

    private static final HashMap<String,List<AXmlActivityInfo>> AXML_CACHE = new HashMap();

    public static void init(ApkPluginInfo apk){
        List <AXmlActivityInfo> infos = new LinkedList<>();
        InputStream in = null;
        try {
            ZipFile zipFile = new ZipFile(apk.getApkUri());
            ZipEntry axmlEntry = zipFile.getEntry("AndroidManifest.xml");
            in = zipFile.getInputStream(axmlEntry);
            AXmlResourceParser xmlParser = new AXmlResourceParser();
            xmlParser.open(in);
            int event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        if(AXmlElement.ACTIVITY_TAG.equals(xmlParser.getName())){
                            AXmlActivityInfo activityInfo = AXmlActivityInfo.resolve(xmlParser);
                            infos.add(activityInfo);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();   //将当前解析器光标往下一步移
            }
            AXML_CACHE.put(apk.getApkUri(),infos);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 这里只解决intent的隐式跳转
     * @param apk 插件
     * @param intent
     */
    public static boolean resolveIntent(ApkPluginInfo apk, Intent intent){
        if(intent.getComponent() == null){
            List<AXmlActivityInfo> list = AXML_CACHE.get(apk.getApkUri());
            if(list == null)
                return false;
            for(AXmlActivityInfo info : list){
                if(info.resolveIntent(intent)){
                    return true;
                }
            }
        }
        return false;
    }

}
