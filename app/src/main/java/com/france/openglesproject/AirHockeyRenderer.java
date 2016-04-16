package com.france.openglesproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.france.openglesproject.util.ShaderHelper;
import com.france.openglesproject.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;


public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];//用于存储矩阵
    private int uMatrixLocation;//u_Matrix uniform位置
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;//跨距告诉OpenGL每两个顶点在本地内存需要跳过的数据长度

    private final FloatBuffer vertexData;
    private final Context context;

    private int program;
    private int aPositionLocation;
    private int aColorLocation;
    /*
    * surface创建时调用
    * @parem gl10: 1.0遗留 为向下兼容
    * ps:横竖屏切换时，会调用，重新获得OpenGL 上下文会再调用(pause->resume)
    * */
    public AirHockeyRenderer(Context context){
        this.context=context;
        //逆时针 两个三角形去拼接为矩形
        /*
		float[] tableVertices = {
			0f,  0f,
			0f, 14f,
			9f, 14f,
			9f,  0f
		};
         */
        /*
		float[] tableVerticesWithTriangles = {
			// Triangle 1
			0f,  0f,
			9f, 14f,
			0f, 14f,

			// Triangle 2
			0f,  0f,
			9f,  0f,
			9f, 14f
			// Next block for formatting purposes
			9f, 14f,
			, // Comma here for formatting purposes

			// Line 1
			0f,  7f,
			9f,  7f,

			// Mallets
			4.5f,  2f,
			4.5f, 12f
		};
         */
        float[] tableVerticesWithTriangles = {
                // Order of coordinates: X, Y, Z, W, R, G, B

                // Triangle Fan
                0f,    0f, 0f, 1.5f,   1f,   1f,   1f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
                0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
                0f,  0.4f, 0f, 1.75f, 1f, 0f, 0f
        };
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)//分配本地内存
                .order(ByteOrder.nativeOrder())//保证一个平台使用同样的排序：按照本地字节序组织内容
                .asFloatBuffer();//不操作字节 而是希望调用浮点数
        vertexData.put(tableVerticesWithTriangles);//把数据从Dalvik内存复制到本地内存 进程结束时释放内存


    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        Log.i("zjx1","onSurfaceCreated");
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);
        //获取引用
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program=ShaderHelper.linkProgram(vertexShader,fragmentShader);
        ShaderHelper.validateProgram(program);
        //告诉OpenGL绘制东西要屏幕上要使用这里的program
        glUseProgram(program);
        //获取uniform和attribute的位置
        //uColorLocation = glGetUniformLocation(program, U_COLOR); 改为
        aColorLocation = glGetAttribLocation(program,A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        //关联属性与顶点数据的数组
        vertexData.position(0);//确保本地内存的vertexData从数组头开始读取
        /**
         * glVertexAttribPointer (int index, int size, int type, boolean normalized, int stride, Buffer ptr)
         * @param index :我们把数据传入attribute位置，指向glGetAttribLocation中获取的位置
         * @param size:这里我们只用了2个分量(x,y)，注意在着色器中a_Pointion被定义为vec4
         * @param type:数据类型
         * @param normalized:只要使用整形该参数才有意义
         * @param stride:跨距
         * @param ptr:去哪读数据
         * 注意传入正确的参数，否则难以调试
         */
//        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
//                false, 0, vertexData); 改为
        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        //告诉OpenGL去aPositionLocation寻找数据
        glEnableVertexAttribArray(aPositionLocation);

        //把顶点数据和a_Color关联起来
        vertexData.position(POSITION_COMPONENT_COUNT);
        //顶点数据的颜色值从2位置开始，有3个，GL_FLOAT类型,跨距STRIDE,
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        //告诉OpenGL去aColorLocation寻找颜色数据
        glEnableVertexAttribArray(aColorLocation);
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
    }

    /*
    * surface尺寸变化时调用,
    * ps:横竖屏切换时，surface尺寸会发生变化
    * */
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //设置视口尺寸，渲染surface的大小
        Log.i("zjx2","onSurfaceChanged");
        glViewport(0,0,width,height);
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            // Landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }
    /*
    *每绘制一帧，都会被GLSurfaceView调用，所以在该方法一定要绘制东西，即使只是clear screen
    * because after this method, 渲染缓冲区会被交换显示到屏幕上，
    * 否则会出现闪屏效果
    * */
    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);
        // Assign the matrix
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        //更新着色器中u_Color中的值(在onSurfaceCreated中我们已经获取到uniform的位置并存入uColorLocation)
        // uniform没有默认值 所以这里我们必须指定
        //先随便设置个RGBA
        /*
        * glDrawArrays (int mode, int first, int count)
        * mode:我们要画的类型
        * first:从顶点数组vertexData那个位置开始找
        * count:读入一个顶点 这里读6个 画出三角形扇
        * */
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        //一些其他的绘制
        // Draw the center dividing line.
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet blue.
        glDrawArrays(GL_POINTS, 8, 1);

        // Draw the second mallet red.
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
