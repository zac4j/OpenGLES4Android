attribute vec4 a_Position;

void main()
{
  // Copy the position to gl_Position
  gl_Position = a_Position;
}