package com.gpower.regionutils;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gpower.windowdisplaycutout.WindowCutoutUtils;

/**
 * @author Secret
 * @since 2018/10/9
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCutoutUtils.openFullScreenModel(this);
        WindowCutoutUtils.getStatusBarHeight(this);
        setContentView(R.layout.activity_main);
        //for AppCompatActivity
        if(null != getSupportActionBar()){
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowCutoutUtils.getScreenNotchParameter(this);
            }
        }
    }
}
