package com.zac4j.opengl.object;

import com.zac4j.opengl.Constants;
import com.zac4j.opengl.data.VertexArray;
import com.zac4j.opengl.program.ColorShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Air Hockey Mallet Class
 * Created by zac on 16-9-8.
 */
public class Mallet {

  // 位置坐标由两个浮点数组成
  private static final int POSITION_COMPONENT_COUNT = 2;

  // 颜色由三个浮点数组成
  private static final int COLOR_COMPONENT_COUNT = 3;

  // 步长
  private static final int STRIDE =
      (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

  private static final float[] VERTEX_DATA = {
      // Order of coordinates: X, Y, R, G, B
      0f, -0.4f, 0f, 0f, 1f, 0f, 0.4f, 1f, 0f, 0f
  };

  private final VertexArray mVertexArray;

  public Mallet() {
    mVertexArray = new VertexArray(VERTEX_DATA);
  }

  public void bindData(ColorShaderProgram colorProgram) {
    mVertexArray.setVertexAttributePointer(0, colorProgram.getPositionAttributeLocation(),
        POSITION_COMPONENT_COUNT, STRIDE);

    mVertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT,
        colorProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
  }

  /**
   * 绘制点
   */
  public void draw() {
    glDrawArrays(GL_POINTS, 0, 2);
  }

}
