# PhantomTest

满帮集团插件化框架[Phantom](https://github.com/ManbangGroup/Phantom)使用演示

## Phantom插件化演示（请star支持）

![](https://github.com/xuexiangjys/PhantomTest/blob/master/img/demo.gif)

## 演示demo下载

[![](https://img.shields.io/badge/宿主apk-1.8M-blue.svg)](https://github.com/xuexiangjys/PhantomTest/blob/master/apk/com.xuexiang.phantomtest_1.0.apk)

[![](https://img.shields.io/badge/插件apk-1M-blue.svg)](https://github.com/xuexiangjys/PhantomTest/blob/master/apk/com.xuexiang.xqrcodetest_1.0.apk)

注意：请将插件apk拷贝至sdcard下。

## Phantom介绍

> Phantom 是满帮集团开源的一套稳定、灵活、兼容性好的 Android 插件化方案。

## Phantom特点

* 兼容性好：零 Hook，没有调用系统的 hidden API，完美兼容 Android 9.0
* 功能完整：插件支持独立应用的绝大部分特性
* 稳定可靠：历经货车帮旗下多款产品 50+ 插件两年多千万级用户验证（稳定性和兼容性指标都在 4 个 9 以上）
* 部署灵活：宿主无需升级（无需在宿主 AndroidManifest.xml 中预埋组件），即可支持插件新增组件，甚至新增插件
* 易于集成：无论插件端还是宿主端，只需『数行』就能完成接入，改造成本低

## Phantom不支持项

* 暂时不支持 Gradle 4.x + Android Gradle Plugin 3.x
* 暂时不支持 Android DataBinding
* 不支持ContentProvider
* [其他不支持项](https://github.com/ManbangGroup/Phantom/blob/master/docs/known-issues.md)

## Phantom 与主流开源插件框架的对比【官方给的】

| 特性 | [Atlas][1] | [Small][2] | [VirtualAPK][3] | [RePlugin][4] | [Phantom][5] |
| ---- | ---- | ---- | ---- | ---- | ---- |
| Hook 数量 | 较多 | 较少 | 较少 | 仅一处 | **零** |
| 四大组件 | 全支持 | 只支持 `Activity` | 全支持 | 全支持 | 除 `ContentProvider` 外，全支持 |
| 剔除公共库 | 支持 | 支持 | 支持 | 不支持 | 支持 |
| 兼容性适配 | 高 | 高 | 高 | 高 | 非常高 |
| 插件热更新 | 不支持 | 不支持 | 不支持 | 不支持 | 支持 |
| 插件快速部署 | 不支持 | 不支持 | 不支持 | 支持 | 支持 |
| 插件宿主通信 | 一般 | 一般 | 弱 | 一般 | 强 |


## Phantom集成指南

### 宿主程序

> 宿主程序相当于一个空壳容器，用于加载插件APK，需要注意的是，插件使用到的权限，宿主也要同步获取，否则将无法正常运行。

#### 配置Gradle脚本

> 特别需要注意的是，这里一定要使用 `Gradle 3.3 + Android Gradle Plugin 2.3.3`的方式，否则会

1.在宿主项目根目录下的 build.gradle 中增加宿主 gradle 依赖

```
buildscript {
    dependencies {
      classpath 'com.wlqq.phantom:phantom-host-gradle:3.0.0'
    }
}
```
2.在宿主项目的 App 模块的 build.gradle 中增加宿主 library 依赖，并应用宿主 gradle 依赖包含的 gradle 插件 `com.wlqq.phantom.host`。

```
apply plugin: 'com.wlqq.phantom.host'

dependencies {
    compile 'com.wlqq.phantom:phantom-host-lib:3.0.0'
}
```

#### 插件初始化

在Application的onCreate初始化 Phantom 插件框架.详细配置可[点击查看](https://github.com/ManbangGroup/Phantom/blob/master/docs/phantom-core-init.md)

```
public class YourApplication extends Application {
    @Override
    public void onCreate() {
       super.onCreate();
       //初始化Phantom框架，并加载已经安装的插件
       PhantomCore.getInstance().init(this, new PhantomCore.Config());
    }
}
```

#### 插件安装和卸载

> 详细说明请见[官方文档](https://github.com/ManbangGroup/Phantom/blob/master/docs/plugin-management.md)

1.插件安装

* installPlugin: 从SDCard卡上安装插件。

```
PhantomCore.getInstance().installPlugin(Environment.getExternalStorageDirectory() + "/com.xuexiang.xqrcodetest_1.0.apk");
```

* installPluginFromAssets: 从assets下安装插件

```
PhantomCore.getInstance().installPluginFromAssets("plugins/com.wlqq.phantom.plugin.view_1.0.0.apk")
```

2.如何判断插件是否已安装

使用PhantomCore.getInstance().isPluginInstalled("插件包名")判断。

```
PhantomCore.getInstance().isPluginInstalled("com.xuexiang.xqrcodetest")
```

3.插件卸载

使用PhantomCore.getInstance().uninstallPlugin("插件包名")来卸载插件。

```
PhantomCore.getInstance().uninstallPlugin("com.xuexiang.xqrcodetest");
```

4.插件启用

插件安装完之后，启动插件代表该插件将被立即加载到内存中，调用 PluginInfo 接口中 start() 即可.

```
InstallResult ret = PhantomCore.getInstance().installPlugin(Environment.getExternalStorageDirectory() + "/com.xuexiang.xqrcodetest_1.0.apk");
// 插件安装成功后启动插件(执行插件的 Application#onCreate 方法)
if (ret.isSuccess() && ret.plugin.start()) {
    Intent intent = new Intent();
    // 指定插件 Activity 所在的插件包名以及 Activity 类名
    intent.setClassName("com.xuexiang.xqrcodetest", "com.xuexiang.xqrcodetest.Main2Activity");
    PhantomCore.getInstance().startActivity(this, intent);
}
```

### 插件程序

> 插件程序作为一个独立的apk，可以独立安装，也可以被宿主程序安装加载使用。需要注意的是，插件使用到的权限，宿主也要同步获取，否则将无法正常运行。

#### 配置Gradle脚本

> 特别需要注意的是，这里一定要使用 `Gradle 3.3 + Android Gradle Plugin 2.3.3`的方式，否则会

1.在插件项目根目录下的 build.gradle 中增加插件 gradle 依赖

```
buildscript {
    dependencies {
      classpath 'com.wlqq.phantom:phantom-plugin-gradle:3.0.0'
    }
}
```

2.在插件项目 App 模块的 build.gradle 中增加插件 library 依赖，并应用宿主 gradle 依赖包含的 gradle 插件 `com.wlqq.phantom.plugin`

```
apply plugin: 'com.wlqq.phantom.plugin'

android {
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            // Phantom 混淆配置文件
            proguardFile 'proguard-phantom.pro'
        }
    }
}

dependencies {
    provided 'com.wlqq.phantom:phantom-plugin-lib:3.0.0'
    compile 'com.android.support:support-v4:28.0.0'
}
```

3.配置PhantomPlugin插件的参数

着重注意`hostApplicationId`和`hostAppLauncherActivity`这两个参数，是需要你配成自己的宿主信息。

```
phantomPluginConfig {
    // BEGIN 剔除公共库配置
    // 若插件中有使用 support-v4 ，则需要剔除掉(必须)
    excludeLib "com.android.support:support-v4:28.0.0"
    // END

    // BEGIN 生成插件额外的混淆配置文件，避免因剔除公共库引起的混淆问题
    libraryJarsProguardFile file('proguard-phantom.pro')
    // END

    // BEGIN 快速部署插件配置
    // 宿主包名,这里需要改成你自己的
    hostApplicationId = "com.xuexiang.phantomtest"
    // 宿主 launcher Activity full class name
    hostAppLauncherActivity = "com.xuexiang.phantomtest.MainActivity"
    // 插件包名,这里需要改成你自己的
    pluginApplicationId = android.defaultConfig.applicationId
    // 插件版本名
    pluginVersionName = android.defaultConfig.versionName
    // END
}
```

4.在插件 AndroidManifest.xml 中申明对宿主 Phantom 插件框架版本依赖（目前版本名是 3.0.0，对应版本号为 30000）

```
<application>
    ...

    <meta-data
        android:name="phantom.service.import.PhantomVersionService"
        android:value="30000" />
</application>
```

### 编译

#### 编译插件

与编译独立 APK 相同，如：

* `./gradlew assembleDebug`
* `./gradlew assembleRelease`

#### 编译插件并将插件 APK 安装到宿主

插件端使用的 Gradle 插件会自动为项目的 variant 生成相应的插件安装 task ，格式为 `phInstallPlugin${variant}` ，例如：

* `./gradlew phInstallPluginDebug`
* `./gradlew phInstallPluginRelease`

注意：以上命令会自动将插件的apk文件push到手机的sdcard目录。


## 其他说明

* [四大组件](https://github.com/ManbangGroup/Phantom/blob/master/docs/components.md)
* [Phantom 通信服务](https://github.com/ManbangGroup/Phantom/blob/master/docs/phantom-service.md)
* [插件 meta-data](https://github.com/ManbangGroup/Phantom/blob/master/docs/android-manifest-metadata.md)
* [Phantom 安全签名校验](https://github.com/ManbangGroup/Phantom/blob/master/docs/security.md)
* [Native 支持](https://github.com/ManbangGroup/Phantom/blob/master/docs/native.md)

## 联系方式

Phantom官方交流 QQ 群号：**690051836**

[![](https://img.shields.io/badge/点击一键加入QQ交流群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

![](https://github.com/xuexiangjys/Resource/blob/master/img/qq/qq_group.jpg)

[1]: https://github.com/alibaba/atlas "Atlas"
[2]: https://github.com/wequick/Small "Small"
[3]: https://github.com/didi/VirtualAPK "VirtualAPK"
[4]: https://github.com/Qihoo360/RePlugin "RePlugin"
[5]: https://github.com/ManbangGroup/Phantom "Phantom"
[6]: https://github.com/apache/maven "Maven"
[7]: https://github.com/zafarkhaja/jsemver "jsemver"