package com.zac4j.opengl.program;

import android.content.Context;
import com.zac4j.opengl.util.ShaderHelper;
import com.zac4j.opengl.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * Shader Program Class
 * Created by zac on 16-9-8.
 */
public class ShaderProgram {

  // Uniform 常量
  protected static final String U_MATRIX = "u_Matrix";
  protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
  protected static final String U_COLOR = "u_Color";

  // Attribute 常量
  protected static final String A_POSITION = "a_Position";
  protected static final String A_COLOR = "a_Color";
  protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

  // Shader Program
  protected final int program;

  protected ShaderProgram(Context context, int vertexShaderResId, int fragmentShaderResId) {
    program = ShaderHelper.buildProgram(
        TextResourceReader.readTextFileFromResource(context, vertexShaderResId),
        TextResourceReader.readTextFileFromResource(context, fragmentShaderResId));
  }

  /**
   * 应用当前 program
   */
  public void useProgram() {
    glUseProgram(program);
  }

}
