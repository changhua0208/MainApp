package com.jch.plugin;

/**
 * @author changhua.jiang
 * @since 2017/9/22 下午8:39
 */

public class ResourceManager {

//    Map<String,AssetManager> mAssetManagers = new HashMap<>();
//    Map<String,Resources> mResources = new HashMap<>();
//    Map<String,Resources.Theme> mTheme = new HashMap<>();
//
//    private String DEFAULT_KEY = "Default";
//
//    private static final ResourceManager instance = new ResourceManager();
//
//    private ResourceManager(){
//
//    }
//
//    public static ResourceManager getInstance(){
//        return instance;
//    }
//
//    public Resources getResources(ApkPluginInfo info){
//        if(info != null) {
//            return mResources.get(info.getApkUri());
//        }
//        else{
//            return mResources.get(DEFAULT_KEY);
//        }
//    }
//
//    public Resources.Theme getTheme(ApkPluginInfo info){
//        if(info != null) {
//            return mTheme.get(info.getApkUri());
//        }
//        else{
//            return mTheme.get(DEFAULT_KEY);
//        }
//    }
//
//    public void init(Resources superRes,ApkPluginInfo info){
//        //如果没有插件，就用当前的resource
//        if(info != null) {
//            if(mAssetManagers.containsKey(info.getApkUri())){
//                return;
//            }
//            try {
//                AssetManager assetManager = AssetManager.class.newInstance();
//                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
//                addAssetPath.invoke(assetManager, info.getApkUri());
//                mAssetManagers.put(info.getApkUri(),assetManager);
//                mResources.put(info.getApkUri(),new Resources(assetManager, superRes.getDisplayMetrics(),
//                        superRes.getConfiguration()));
//
//
//            } catch (Exception e) {
//                mAssetManagers.remove(info.getApkUri());
//                mResources.remove(info.getApkUri());
//                mTheme.remove(info.getApkUri());
//            }
//        }
//        else{
//            mAssetManagers.put(DEFAULT_KEY,superRes.getAssets());
//            mResources.put(DEFAULT_KEY,superRes);
//            mTheme.put(DEFAULT_KEY,superRes.newTheme());
//        }
//    }
//
//    public AssetManager getAssetManager(ApkPluginInfo info){
//        if(info != null) {
//            return mAssetManagers.get(info.getApkUri());
//        }
//        else{
//            return mAssetManagers.get(DEFAULT_KEY);
//        }
//    }


}
