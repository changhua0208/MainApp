# 支持情况

1. android插件化代码，支持activity代理，支持直接发起activity，支持scheme发起。支持动态链接库。

2. 第一次运行，需要在android studio中rebuild一下，以生成插件

3. 支持android 8.0

4. 修改插件代码后，在android studio rebuild一下，然后运行。

5. 修改宿主程序时，直接运行即可。

# 新建插件

1. new module -> Phone & Tablet Module -> test2
2. 修改build.gradle

```groovy
apply plugin: 'com.android.application'

//生成的apk名称
ext.apkName="test2"
//在as中的module名称
ext.moduleName="test2"
//插件拷贝以及implementationInner方法定义
apply from:"../common.gradle"
//编译环境，公共依赖
apply from:"../config.gradle"
//application id
def appId="com.jch.test2"

android {
    //统一编译环境
    compileSdkVersion ac.compileSdkVersion
    buildToolsVersion ac.buildToolsVersion

    defaultConfig {
        //module单独运行时有用，当做插件时，没有作用
        applicationId "${appId}"
        minSdkVersion ac.minSdkVersion
        targetSdkVersion ac.targetSdkVersion
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }


}

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation dep.appcompatV7
    //插件依赖工程，在test2只当插件是，参与编译，但是不参与打包
    implementationInner dep.plugin
}
```

3. 修改MainActivity 继承 PluginActivity

```java
public class MainActivity extends PluginActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

4. 注册插件到宿主中

```xml
<?xml version="1.0" encoding="utf-8"?>
<Plugins xmlns:android="http://schemas.android.com/apk/res/android">
    <Plugin name="test" packageName="com.jch.test" launcher="MainActivity" apkPath="apk" nativeLib="lib" icon="mipmap/ic_launcher"/>
    <Plugin name="shape" packageName="com.jch.shape" launcher="MainActivity" apkPath="apk" nativeLib="lib" icon="mipmap/ic_launcher"/>
    <Plugin name="test2" packageName="com.jch.test2" launcher="MainActivity" apkPath="apk" nativeLib="lib" icon="mipmap/ic_launcher"/>
</Plugins>
```

5. 在android studio上rebuild一下，完成后，在MainApp的assert中可以看到插件test2

6. 运行MainApp