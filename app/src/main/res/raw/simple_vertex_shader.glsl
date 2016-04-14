//类似c语言
attribute vec4 a_Position;

void main(){
    gl_Position = a_Position;//OpenGL会把gl_Position中存储的值作为当前顶点的最终位置。
}