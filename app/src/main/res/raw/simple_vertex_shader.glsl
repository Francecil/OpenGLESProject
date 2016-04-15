//类似c语言
attribute vec4 a_Position;

void main(){
    gl_Position = a_Position;//OpenGL会把gl_Position中存储的值作为当前顶点的最终位置。
    gl_PointSize = 10.0;//OpenGL将点分解为一些以gl_Position为中心的四边形，每个四边形长度为 gl_PointSize
}