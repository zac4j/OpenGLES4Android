// Define the default precision for all floating point dat types in the fragment shader.
// mediump indicate medium precision, beside there has lowp, highp, and vertex shaders
// has the highp by default.
precision mediump float;

uniform vec4 u_Color;

void main()
{
  gl_FragColor = u_Color;
}