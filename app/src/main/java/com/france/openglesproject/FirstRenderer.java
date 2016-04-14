package com.france.openglesproject;

import android.opengl.GLSurfaceView;
import android.util.Log;

import static android.opengl.GLES20.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2016/3/29.
 * 采用静态import GLES20 所以下面的glClearColor等静态方法/变量 无需写类名
 * GLSurfaceView 会在单独的线程调用渲染器的方法
 * 后台渲染线程和主线程(UI线程)通信可以用runOnUIThread()来传递event
 */
public class FirstRenderer implements GLSurfaceView.Renderer {
    /*
    * surface创建时调用
    * @parem gl10: 1.0遗留 为向下兼容
    * ps:横竖屏切换时，会调用，重新获得OpenGL 上下文会再调用(pause->resume)
    * */
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.i("zjx1","onSurfaceCreated");
        glClearColor(1.0f,1.0f,0.0f,0.5f);
    }

    /*
    * surface尺寸变化时调用,
    * ps:横竖屏切换时，surface尺寸会发生变化
    * */
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //设置视口尺寸，渲染surface的大小
        Log.i("zjx2","onSurfaceChanged");
        glViewport(0,0,width/2,height/2);
    }
    /*
    *每绘制一帧，都会被GLSurfaceView调用，所以在该方法一定要绘制东西，即使只是clear screen
    * because after this method, 渲染缓冲区会被交换显示到屏幕上，
    * 否则会出现闪屏效果
    * */
    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.i("zjx3","onDrawFrame");
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
