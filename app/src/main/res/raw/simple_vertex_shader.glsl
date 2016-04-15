uniform mat4 u_Matrix;//4x4的矩阵

attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

void main()                    
{                            
    v_Color = a_Color;
	  
    //gl_Position = a_Position;
    gl_Position = u_Matrix * a_Position;  //进行正交投影
    gl_PointSize = 10.0;          
}          