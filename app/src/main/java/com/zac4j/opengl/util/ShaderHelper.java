package com.zac4j.opengl.util;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * ShaderHelper class help to create new Shader obj and compile shader code.
 * Created by zac on 16-9-5.
 */
public class ShaderHelper {

  private static final String TAG = "ShaderHelper";

  /**
   * 编译 Vertex Shader
   *
   * @param shaderCode Shader 源码
   * @return 编译成功返回 Shader对象的Id，否则返回 0
   */
  public static int compileVertexShader(String shaderCode) {
    return compileShader(GL_VERTEX_SHADER, shaderCode);
  }

  /**
   * 编译 Fragment Shader
   *
   * @param shaderCode Shader 源码
   * @return 编译成功返回 Shader对象的Id，否则返回 0
   */
  public static int compileFragmentShader(String shaderCode) {
    return compileShader(GL_FRAGMENT_SHADER, shaderCode);
  }

  /**
   * 接收 Shader 的类别和 Shader 的源码并做编译
   *
   * @param type Shader 的类别， 分为 GL20.GL_VERTEX_SHADER 或 GL_FRAGMENT_SHADER
   * @param shaderCode Shader 源码
   * @return 编译成功返回 Shader对象的Id，否则返回 0
   */
  private static int compileShader(int type, String shaderCode) {
    // 创建新的 shader 对象，并将 shader 对象的 id 保存在 shaderObjectId 变量中
    final int shaderObjectId = glCreateShader(type);

    // 返回 0 表示创建失败
    if (shaderObjectId == 0) {
      if (LoggerConfig.ON) {
        Log.w(TAG, "Could not create new shader.");
      }
      return 0;
    }

    // 向 shader对象上传 shader 源码
    glShaderSource(shaderObjectId, shaderCode);

    // 编译之前上传 shader 源码
    glCompileShader(shaderObjectId);

    // 检查是否编译成功
    final int[] compileStatus = new int[1];
    glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

    // a good practice: --> 获取 shader 的 log 信息
    if (LoggerConfig.ON) {
      Log.v(TAG, "Results of compiling source: " + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(
          shaderObjectId));
    }

    // 为 0 表示编译失败
    if (compileStatus[0] == 0) {
      glDeleteShader(shaderObjectId);

      if (LoggerConfig.ON) {
        Log.w(TAG, "Compilation of shader failed.");
      }
      return 0;
    }

    return shaderObjectId;
  }

  /**
   * 将 vertex shader 和 fragment shader 连接到同一 Program 对象
   *
   * @param vertexShaderId vertex shader id
   * @param fragmentShaderId fragment shader id
   * @return 连接成功返回 Program 对象 id ，否则返回 0
   */
  public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

    // 创建新的 Program 对象并将其引用保存在变量 programObjectId 中
    final int programObjectId = glCreateProgram();

    if (programObjectId == 0) {
      if (LoggerConfig.ON) {
        Log.w(TAG, "Could not create new program.");
      }

      return 0;
    }

    // 将 vertex, fragment shader 连接到同一 Program 对象
    glAttachShader(programObjectId, vertexShaderId);
    glAttachShader(programObjectId, fragmentShaderId);

    // 连接 shader 与 program 对象
    glLinkProgram(programObjectId);

    // 检测连接状态，并保存到长度为1的 linkStatus 数组中
    final int[] linkStatus = new int[1];
    glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

    // 获取 program 的 log 信息
    if (LoggerConfig.ON) {
      Log.v(TAG, "Results of linking program: \n" + glGetProgramInfoLog(programObjectId));
    }

    // 连接状态为 0 表示失败
    if (linkStatus[0] == 0) {
      glDeleteProgram(programObjectId);
      if (LoggerConfig.ON) {
        Log.w(TAG, "Linking of program failed.");
      }
      return 0;
    }

    return programObjectId;
  }

  /**
   * 验证 Program 是否有效
   *
   * @param programObjectId program 对象 id
   * @return 有效返回 true， 否则返回 false
   */
  public static boolean validateProgram(int programObjectId) {
    glValidateProgram(programObjectId);

    final int[] validateStatus = new int[1];
    glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
    Log.v(TAG, "Results of validating program: " + validateStatus[0] +
        "\nLog: " + glGetProgramInfoLog(programObjectId));

    return validateStatus[0] != 0;
  }

  /**
   * 构造 OpenGL Program对象
   * @param vertexShaderSource 顶点 Shader 资源
   * @param fragmentShaderSource 区块 Shader 资源
   * @return 连接顶点和区块 Shader 后的 Program 对象的引用
   */
  public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
    int program;

    // 编译Shader
    int vertexShader = compileVertexShader(vertexShaderSource);
    int fragmentShader = compileFragmentShader(fragmentShaderSource);

    // 连接 Shader 到 Program
    program = linkProgram(vertexShader, fragmentShader);

    if (LoggerConfig.ON) {
      validateProgram(program);
    }

    return program;
  }
}
