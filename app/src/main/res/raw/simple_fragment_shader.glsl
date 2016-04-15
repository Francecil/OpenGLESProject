//定义浮点数精度：lowp/mediumn/highp 低/中/高 精度 精度越高性能越低
//顶点着色器默认是highp:顶点位置精度重要
precision mediump float;
uniform vec4 u_Color;

void main(){
    gl_FragColor = u_Color;//OpenGL会把gl_FragColor中存储的这个颜色作为当前片段的最终颜色
}