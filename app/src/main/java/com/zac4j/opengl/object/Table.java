package com.zac4j.opengl.object;

import com.zac4j.opengl.Constants;
import com.zac4j.opengl.data.VertexArray;
import com.zac4j.opengl.program.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Air Hockey Table Class
 * Created by zac on 16-9-8.
 */
public class Table {

  // 位置坐标由两个浮点数组成
  private static final int POSITION_COMPONENT_COUNT = 2;

  // 纹理坐标同样由两个浮点数组成
  private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

  // Native 层坐标数据的步长
  private static final int STRIDE =
      (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

  private static final float[] VERTEX_DATA = {
      // 坐标的顺序: X, Y, S, T

      // 三角扇形 Triangle Fan
      0f, 0f, 0.5f, 0.5f, -0.5f, -0.8f, 0f, 0.9f, 0.5f, -0.8f, 1f, 0.9f, 0.5f, 0.8f, 1f, 0.1f,
      -0.5f, 0.8f, 0f, 0.1f, -0.5f, -0.8f, 0f, 0.9f
  };

  private final VertexArray mVertexArray;

  public Table() {

    mVertexArray = new VertexArray(VERTEX_DATA);
  }

  /**
   * 绑定数据
   * @param textureProgram 纹理着色对象
   */
  public void bindData(TextureShaderProgram textureProgram) {
    mVertexArray.setVertexAttributePointer(0, textureProgram.getPositionAttributeLocation(),
        POSITION_COMPONENT_COUNT, STRIDE);

    mVertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT,
        textureProgram.getTextureCoordsAttributeLocation(),
        TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
  }

  /**
   * 绘制三角扇形
   */
  public void draw() {
    glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
  }
}
