package com.france.openglesproject;

import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;


public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;//本地内存存储数据
    /*
    * surface创建时调用
    * @parem gl10: 1.0遗留 为向下兼容
    * ps:横竖屏切换时，会调用，重新获得OpenGL 上下文会再调用(pause->resume)
    * */
    public AirHockeyRenderer(){
        //逆时针 两个三角形去拼接为矩形
        float[] tableVerticesWithTriangles ={
                //Triangle1
                0f,0f,
                9f,14f,
                0f,14f,
                //Triangle2
                0f,0f,
                9f,0f,
                9f,14f,
                //line
                0f,7f,
                9f,7f,
                //Mallets
                4.5f,2f,
                4.5f,12f
        };
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)//分配本地内存
                .order(ByteOrder.nativeOrder())//保证一个平台使用同样的排序：按照本地字节序组织内容
                .asFloatBuffer();//不操作字节 而是希望调用浮点数
        vertexData.put(tableVerticesWithTriangles);//把数据从Dalvik内存复制到本地内存 进程结束时释放内存


    }
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
