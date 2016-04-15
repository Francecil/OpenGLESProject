package com.france.openglesproject.util;

/**
 * Created by Administrator on 2016/4/15.
 */

import android.util.Log;

import static android.opengl.GLES20.*;
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }
    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }
    /**
     * 编译着色器, 返回 OpenGL object ID.
     */
    private static int compileShader(int type, String shaderCode) {
        // new 着色器对象
        //shaderObjectId 是OpenGL对象的引用
        final int shaderObjectId = glCreateShader(type);
        //内部实现是返回0而不是抛异常
        if(shaderObjectId == 0){
            Log.w(TAG,"没有创建着色器对象");
            return 0;
        }
        //把着色器源代码传到着色器对象中
        glShaderSource(shaderObjectId,shaderCode);
        //编译着色器
        glCompileShader(shaderObjectId);////shaderObjectId 保持着对OpenGL对象(该着色器对象)的引用
        //得到编译状态
        final int[] compileStatus = new int[1];
        //存入compileStatus数组的第0个元素
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);
        //也可以选择获得更多的编译信息
        Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                + glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            // 编译失败，删除对象
            glDeleteShader(shaderObjectId);
            Log.w(TAG, "Compilation of shader failed.");
            return 0;
        }
        //编译成功
        return shaderObjectId;
    }
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        //新建一个program对象，用于后面链接2个着色器
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.w(TAG, "Could not create new program");
            return 0;
        }
        //附上着色器
        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentShaderId);
        //链接
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Log.v(TAG, "Results of linking program:\n"
                    + glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            Log.w(TAG, "Linking of program failed.");
            return 0;
        }
        return programObjectId;
    }
    public static boolean validateProgram(int programObjectId) {
        //检查是否有效
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
