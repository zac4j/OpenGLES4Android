uniform mat4 u_Matrix;
attribute vec4 a_Position;

void main()
{
  // Copy the position to gl_Position
  gl_Position = u_Matrix * a_Position;
  //gl_PointSize = 10.0;
}