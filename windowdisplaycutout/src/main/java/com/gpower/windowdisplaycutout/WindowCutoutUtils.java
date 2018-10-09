package com.gpower.windowdisplaycutout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.util.List;

/**
 * @author Secret
 * @since 2018/10/9
 */
public class WindowCutoutUtils {

    private static final String TAG = WindowCutoutUtils.class.getName();
    private static boolean DEBUG = true;

    public static void setDEBUG(boolean DEBUG) {
        WindowCutoutUtils.DEBUG = DEBUG;
    }

    /**
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (DEBUG) {
            Log.e(TAG, "statusBarHeight==" + result);
        }
        return result;
    }

    /**
     * 适配刘海屏
     *
     * @param ac activity
     * @see WindowManager.LayoutParams#LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES  状态栏为白色
     * @see WindowManager.LayoutParams#LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER  不使用刘海屏
     * @see WindowManager.LayoutParams#LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT  屏幕上面有空隙，呈现黑色
     * <p>
     * 1、使用LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES模式
     * 2、设置沉浸式布局
     * 3、计算状态栏高度进行布局（永远不要假设状态栏的高度）.如果有特殊UI要求，则可以使用getScreenNotchParameter(Activity ac)方法获得
     * 刘海屏的坐标，完成UI
     * @see #getScreenNotchParameter(Activity)
     */
    public static void openFullScreenModel(Activity ac) {
        /**
         * if a activity instanceof AppCompatActivity,you should use getSupportActionBar().hide() after setContentView(int layoutResId)
         */
        if (!(ac instanceof AppCompatActivity)) {
            ac.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        hideStatusBar(ac);
        hideBottomUIMenu(ac);
    }

    private static void hideStatusBar(Activity ac) {
        WindowManager.LayoutParams attrs = ac.getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        /**
         * 添加WindowManager.LayoutParams.FLAG_FULLSCREEN 属性
         */
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        ac.getWindow().setAttributes(attrs);
    }

    private static void hideBottomUIMenu(Activity ac) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            View v = ac.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = ac.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 通过SDK 28新增的API，可以获得屏幕凸凹口即刘海屏上的每一个凸凹的矩形。
     * @see DisplayCutout#getSafeInsetLeft() 安全区域距屏幕左边的距离
     * @see DisplayCutout#getSafeInsetRight() () 安全区域距屏幕右边的距离
     * @see DisplayCutout#getSafeInsetTop() () 安全区域距屏幕上边的距离
     * @see DisplayCutout#getSafeInsetBottom() () 安全区域距屏幕下边的距离
     *
     * @param ac activity
     */
    @RequiresApi(Build.VERSION_CODES.P)
    public static List<Rect> getScreenNotchParameter(Activity ac) {
        View decorView = ac.getWindow().getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        if(null != rootWindowInsets){
            DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
            if(null != displayCutout){
                List<Rect> notchRectList = displayCutout.getBoundingRects();
                if (null != notchRectList && notchRectList.size() > 0) {
                    if(DEBUG){
                        Log.e(TAG, "safeInsetLeft==" + displayCutout.getSafeInsetLeft());
                        Log.e(TAG, "safeInsetTop==" + displayCutout.getSafeInsetTop());
                        Log.e(TAG, "safeInsetRight==" + displayCutout.getSafeInsetRight());
                        Log.e(TAG, "safeInsetBottom==" + displayCutout.getSafeInsetBottom());
                        for (int i = 0; i < notchRectList.size(); i++) {
                            Log.e(TAG, "left==" + notchRectList.get(i).left);
                            Log.e(TAG, "top==" + notchRectList.get(i).top);
                            Log.e(TAG, "right==" + notchRectList.get(i).right);
                            Log.e(TAG, "bottom==" + notchRectList.get(i).bottom);
                        }
                    }
                    return notchRectList;
                }
            }
        }
        return null;
    }
}
