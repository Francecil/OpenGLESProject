package com.france.openglesproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用GLSurfaceView 初始化OpenGL 为显示GL surface
        glSurfaceView=new GLSurfaceView(this);
        /*
        * 检查OpenGl ES版本
        * */
        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportEs2 =configurationInfo.reqGlEsVersion >=0x20000;
        Log.i("zjx",configurationInfo.reqGlEsVersion+";;;");//输出196608 16进制=0x30000 3.0版本
        if(supportEs2){
            //为了兼容2.0版本的 这边不设置Version为3
            glSurfaceView.setEGLContextClientVersion(2);
            //传入一个自定义Renderer渲染器
            glSurfaceView.setRenderer(new FirstRenderer());
            rendererSet = true ;
            //显示在屏幕上
            setContentView(glSurfaceView);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //暂停surfaceView,释放OpenGl上下文
        if(rendererSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //继续后台渲染线程，续用OpenGL上下文
        if(rendererSet){
            glSurfaceView.onResume();
        }
    }
}
