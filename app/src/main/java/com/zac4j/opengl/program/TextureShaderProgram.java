package com.zac4j.opengl.program;

import android.content.Context;
import com.zac4j.opengl.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * 纹理着色程序
 * Created by zac on 16-9-8.
 */
public class TextureShaderProgram extends ShaderProgram {

  // Uniform locations
  private final int uMatrixLocation;
  private final int uTextureUnitLocation;

  // Attribute locations
  private final int aPositionLocation;
  private final int aTextureCoordsLocation;

  /**
   * 纹理着色程序构造方法
   * @param context 上下文
   */
  public TextureShaderProgram(Context context) {
    super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

    // 获取着色程序中的 uniform location
    uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

    // 获取着色程序中的 attribute location
    aPositionLocation = glGetAttribLocation(program, A_POSITION);
    aTextureCoordsLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
  }

  /**
   * 设置纹理单元
   * @param matrix
   * @param textureId
   */
  public void setUniforms(float[] matrix, int textureId) {

    // 向 shader program 传递矩阵
    glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

    // 设置活动纹理单元为 texture unit 0
    glActiveTexture(GL_TEXTURE0);

    // 绑定纹理到该纹理单元
    glBindTexture(GL_TEXTURE_2D, textureId);

    // Tell the texture uniform sampler to use this texture in the shader by
    // telling it to read from texture unit 0.
    glUniform1i(uTextureUnitLocation, 0);
  }

  /**
   * 获取位置属性 location
   * @return 位置属性 location
   */
  public int getPositionAttributeLocation() {
    return aPositionLocation;
  }

  /**
   * 获取纹理坐标属性 location
   * @return 纹理坐标属性 location
   */
  public int getTextureCoordsAttributeLocation() {
    return aTextureCoordsLocation;
  }


}
