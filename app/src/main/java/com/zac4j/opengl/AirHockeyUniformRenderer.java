package com.zac4j.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.zac4j.opengl.util.LoggerConfig;
import com.zac4j.opengl.util.ShaderHelper;
import com.zac4j.opengl.util.TextResourceReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * OpenGL ES Renderer
 * Created by zac on 16-9-4.
 */
public class AirHockeyUniformRenderer implements GLSurfaceView.Renderer {

  private final Context mContext;

  // 每个点用两个浮点数表示，在vertex shader中定义vec4有四个参数，没有定义的话，默认前三个为0,最后一个为1.
  public static final int POSITION_COMPONENT_COUNT = 2;

  // A float in Java has 32bits of precision, so there are 4 bytes in every float.
  // Java中 float 是32位精度，因此每个 float 类型数据需要 4 字节内存
  public static final int BYTES_PER_FLOAT = 4;

  // Use to store data in native memory.
  // 变量 vertexData 用于保存 native 内存中的数据
  private final FloatBuffer vertexData;

  // 获取并保存 uniform location
  private static final String U_COLOR = "u_Color";
  private int uColorLocation;

  // 获取并保存 attribute location
  private static final String A_POSITION = "a_Position";

  public AirHockeyUniformRenderer(Context context) {

    mContext = context;

    // we can divide rectangle into two triangle
    float[] tableVerticesWithTriangles = {

        // Triangle 1
        -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,

        // Triangle 2
        -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

        // Line 1
        -0.5f, 0f, 0.5f, 0f,

        // Mallets
        0f, -0.25f, 0f, 0.25f,

        // Puck 1 triangle 1
        -0.125f, -0.0625f, 0.125f, 0f, -0.125f, 0f,

        // Puck 1 triangle 2
        -0.125f, -0.0625f, 0.125f, -0.0625f, 0.125f, 0f,

        // Puck 2 triangle 1
        -0.125f, 0f, 0.125f, 0.0625f, -0.125f, 0.0625f,

        // Puck 2 triangle 2
        -0.125f, 0f, 0.125f, 0f, 0.125f, 0.0625f
    };

    // 在 native 空间创建内存并将Java 堆上的数据拷贝到 native 内存中
    vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    vertexData.put(tableVerticesWithTriangles);
  }

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    // the arguments correspond to red, green, blue and alpha.
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    String vertexShaderSource =
        TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
    String fragmentShaderSource =
        TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
    int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
    int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

    int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

    if (LoggerConfig.ON) {
      ShaderHelper.validateProgram(program);
    }

    // OpenGL 使用 program 在屏幕上绘制图形
    glUseProgram(program);

    // 保存 uniform color location
    uColorLocation = glGetUniformLocation(program, U_COLOR);

    // 保存 attribute position location
    int aPositionLocation = glGetAttribLocation(program, A_POSITION);

    // OpenGL 从 buffer 初始位置开始读取数据到 a_Position
    vertexData.position(0);
    glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0,
        vertexData);
    glEnableVertexAttribArray(aPositionLocation);
  }

  @Override public void onSurfaceChanged(GL10 gl10, int i, int i1) {
    // specify the size of the surface for rendering.
    glViewport(0, 0, i, i1);
  }

  @Override public void onDrawFrame(GL10 gl10) {
    // This will wipe out all colors on the screen and fill the screen with the color
    // previously defined by our call to glClearColor()
    glClear(GL10.GL_COLOR_BUFFER_BIT);

    // 初始化 uniform 数据，不同于 attribute， uniform 没有初始值。
    glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);

    // 绘制三角形，从数组0位开始，读两个三角行共6个 vertex.
    glDrawArrays(GL_TRIANGLES, 0, 6);

    glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
    // 绘制分割线，从数组6位开始，共2个vertex
    glDrawArrays(GL_LINES, 6, 2);

    // 绘制蓝槌
    glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
    glDrawArrays(GL_POINTS, 8, 1);

    // 绘制红槌
    glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
    glDrawArrays(GL_POINTS, 9, 1);

    // 绘制冰球上三角
    glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
    glDrawArrays(GL_TRIANGLES, 10, 6);

    // 绘制冰球下三角
    glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
    glDrawArrays(GL_TRIANGLES, 16, 6);

  }
}
