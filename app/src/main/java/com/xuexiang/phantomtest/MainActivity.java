package com.xuexiang.phantomtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wlqq.phantom.library.PhantomCore;
import com.wlqq.phantom.library.pm.InstallResult;
import com.xuexiang.xutil.tip.ToastUtils;

public class MainActivity extends AppCompatActivity {

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_init:
                // 安装打包到宿主 assets 中 plugins 目录下的插件
                InstallResult ret = PhantomCore.getInstance().installPlugin(Environment.getExternalStorageDirectory() + "/com.xuexiang.xqrcodetest_1.0.apk");
//                // 插件安装成功后启动插件(执行插件的 Application#onCreate 方法)
//                if (ret.isSuccess() && ret.plugin.start()) {
//                    Toast.makeText(this, "加载成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.btn_unInstall:
                PhantomCore.getInstance().uninstallPlugin("com.xuexiang.xqrcodetest");
                break;
            case R.id.btn_do:
                if (PhantomCore.getInstance().isPluginInstalled("com.xuexiang.xqrcodetest")) {
                    Intent intent = new Intent();
                    // 指定插件 Activity 所在的插件包名以及 Activity 类名
                    intent.setClassName("com.xuexiang.xqrcodetest", "com.xuexiang.xqrcode.ui.CaptureActivity");
//                    intent.setClassName("com.xuexiang.xqrcodetest", "com.xuexiang.xqrcodetest.Main2Activity");
                    PhantomCore.getInstance().startActivityForResult(this, intent, REQUEST_CODE);
                } else {
                    Toast.makeText(this, "请先加载插件！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            handleScanResult(data);
        }
    }

    /**
     * 处理二维码扫描结果
     * @param data
     */
    private void handleScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getInt("result_type") == 1) {
                    String result = bundle.getString("result_data");
                    ToastUtils.toast("解析结果:" + result, Toast.LENGTH_LONG);
                } else if (bundle.getInt("result_type") == 2) {
                    ToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
                }
            }
        }
    }
}
