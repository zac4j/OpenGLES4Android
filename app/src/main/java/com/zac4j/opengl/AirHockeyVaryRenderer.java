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
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Vertex arrays with position and color
 * Created by zac on 16-9-6.
 */
public class AirHockeyVaryRenderer implements GLSurfaceView.Renderer {

  private final Context mContext;

  // 每个点用两个浮点数表示，在vertex shader中定义vec4有四个参数，没有定义的话，默认前三个为0,最后一个为1.
  public static final int POSITION_COMPONENT_COUNT = 2;

  // Java中 float 是32位精度，因此每个 float 类型数据需要 4 字节内存
  public static final int BYTES_PER_FLOAT = 4;

  // 变量 vertexData 用于保存 native 内存中的数据
  private final FloatBuffer vertexData;

  // 提取 color location 属性key
  private static final String A_COLOR = "a_Color";

  // 每种颜色用3个浮点数表示，在vertex shader中定义vec4有四个参数，没有定义的话，默认前三个为0,最后一个为1.
  private static final int COLOR_COMPONENT_COUNT = 3;

  // 步长包括坐标的2个值以及颜色的3个值
  private static final int STRIDE =
      (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

  // 提取 position location 属性 key
  private static final String A_POSITION = "a_Position";

  public AirHockeyVaryRenderer(Context context) {
    mContext = context;

    float[] tableVerticesWithTriangles = {
        // Order of coordinates: X, Y, R, G, B

        // Triangle Fan, add three additional numbers to each vertex, these numbers represent color.
        0f, 0f, 1f, 1f, 1f, -0.5f, -0.5f, 0.7f, 0.7f, 0.7f, 0.5f, -0.5f, 0.7f, 0.7f, 0.7f, 0.5f,
        0.5f, 0.7f, 0.7f, 0.7f, -0.5f, 0.5f, 0.7f, 0.7f, 0.7f, -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f, 0.5f, 0f, 1f, 0f, 0f,

        // Mallets, 2 points
        0f, -0.25f, 0f, 0f, 1f, 0f, 0.25f, 1f, 0f, 0f
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
        TextResourceReader.readTextFileFromResource(mContext, R.raw.vary_vertex_shader);
    String fragmentShaderSource =
        TextResourceReader.readTextFileFromResource(mContext, R.raw.vary_fragment_shader);
    int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
    int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

    // 连接后的 OpenGL program 对象引用
    int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

    if (LoggerConfig.ON) {
      ShaderHelper.validateProgram(program);
    }

    // 使用此 OpenGL program 在屏幕上绘制图形
    glUseProgram(program);

    // 保存 attribute position location
    int aPositionLocation = glGetAttribLocation(program, A_POSITION);

    // 保存 attribute color location
    int aColorLocation = glGetAttribLocation(program, A_COLOR);

    // OpenGL 从 buffer 初始位置开始读取 position 数据
    vertexData.position(0);
    glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
        vertexData);
    glEnableVertexAttribArray(aPositionLocation);

    // OpenGL 从 color 起始位置开始读取 color 数据
    vertexData.position(POSITION_COMPONENT_COUNT);
    glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
        vertexData);
    glEnableVertexAttribArray(aColorLocation);
  }

  @Override public void onSurfaceChanged(GL10 gl10, int i, int i1) {
    // specify the size of the surface for rendering.
    glViewport(0, 0, i, i1);
  }

  @Override public void onDrawFrame(GL10 gl10) {
    // Clear the rendering surface.
    glClear(GL_COLOR_BUFFER_BIT);

    glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

    glDrawArrays(GL_LINES, 6, 2);

    glDrawArrays(GL_POINTS, 8, 1);

    glDrawArrays(GL_POINTS, 9, 1);
  }
}
