package com.xuexiang.xqrcodetest;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author xuexiang
 * @since 2018/11/16 下午1:52
 */
public class CustomText extends AppCompatTextView {

    public CustomText(Context context) {
        super(context);
        init();
    }

    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        setText("这是我自定义的text");
    }


}
