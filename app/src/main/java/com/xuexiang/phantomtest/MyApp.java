package com.xuexiang.phantomtest;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.wlqq.phantom.library.PhantomCore;
import com.wlqq.phantom.library.PhantomEventCallback;
import com.wlqq.phantom.library.pm.InstallResult;
import com.wlqq.phantom.library.pm.PluginInfo;
import com.xuexiang.xutil.XUtil;

/**
 * @author xuexiang
 * @since 2018/11/16 上午9:49
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XUtil.init(this);

        PhantomCore.getInstance().init(this, new PhantomCore.Config()
                .setCheckVersion(false)
        .setPhantomEventCallback(new PhantomEventCallback() {
            @Override
            public void onPluginInstallStart(String name, boolean fromAssets) {
                Toast.makeText(MyApp.this, "开始安装：" + name + ", fromAssets:" + fromAssets, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPluginInstallSuccess(String name, boolean fromAssets, @NonNull InstallResult installResult) {
                Toast.makeText(MyApp.this, "加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPluginInstallFail(String name, boolean fromAssets, @NonNull InstallResult installResult) {
                Toast.makeText(MyApp.this, "加载失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPluginStartStart(@NonNull PluginInfo pluginInfo, boolean firstStart) {

            }

            @Override
            public void onPluginStartSuccess(@NonNull PluginInfo pluginInfo, boolean firstStart) {

            }

            @Override
            public void onPluginStartFail(@NonNull PluginInfo pluginInfo, boolean firstStart, @NonNull Throwable throwable) {

            }
        }));
    }
}
