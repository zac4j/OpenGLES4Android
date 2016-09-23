package com.zac4j.opengl.program;

import android.content.Context;
import com.zac4j.opengl.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * 色彩着色程序
 * Created by zac on 16-9-8.
 */
public class ColorShaderProgram extends ShaderProgram {

  // Uniform locations
  private final int uMatrixLocation;
  private final int uColorLocation;

  // Attribute locations
  private final int aPositionLocation;
  private final int aColorLocation;

  public ColorShaderProgram(Context context) {
    super(context, R.raw.matrix_vertex_shader, R.raw.vary_fragment_shader);

    // 获取 shader program 的 uniform location
    uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    uColorLocation = glGetUniformLocation(program, U_COLOR);

    // 获取 shader program 的 attribute location
    aPositionLocation = glGetAttribLocation(program, A_POSITION);
    aColorLocation = glGetAttribLocation(program, A_COLOR);
  }

  /**
   * 向 shader program 传递转换矩阵
   *
   * @param matrix 转换矩阵
   */
  public void setUniforms(float[] matrix, float r, float g, float b) {
    glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    glUniform4f(uColorLocation, r, g, b, 1f);
  }

  /**
   * 向 shader program 传递转换矩阵
   *
   * @param matrix 转换矩阵
   */
  public void setUniform(float[] matrix) {
    glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
  }

  /**
   * 获取位置属性 location
   *
   * @return 位置属性 location
   */
  public int getPositionAttributeLocation() {
    return aPositionLocation;
  }

  /**
   * 获取色彩属性 location
   *
   * @return 色彩属性 location
   */
  public int getColorAttributeLocation() {
    return aColorLocation;
  }
}
