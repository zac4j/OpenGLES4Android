package com.zac4j.opengl.object;

import com.zac4j.opengl.data.VertexArray;
import com.zac4j.opengl.program.ColorShaderProgram;
import com.zac4j.opengl.util.Geometry;
import java.util.List;

/**
 * 棒槌类
 * Created by zac on 16-9-22.
 */

public class Mallet {

  private static final int POSITION_COMPONENT_COUNT = 3;

  public final float radius;
  public final float height;

  private final VertexArray mVertexArray;
  private final List<ObjectBuilder.DrawCommand> mDrawList;

  public Mallet(float radius, float height, int numPoints) {
    ObjectBuilder.GeneratedData generatedData =
        ObjectBuilder.createMallet(new Geometry.Point(0f, 0f, 0f), radius, height, numPoints);

    this.radius = radius;
    this.height = height;

    mVertexArray = new VertexArray(generatedData.vertexData);
    mDrawList = generatedData.drawList;
  }

  public void bindData(ColorShaderProgram colorProgram) {
    mVertexArray.setVertexAttributePointer(0, colorProgram.getPositionAttributeLocation(),
        POSITION_COMPONENT_COUNT, 0);
  }

  public void draw() {
    for (ObjectBuilder.DrawCommand drawCommand : mDrawList) {
      drawCommand.draw();
    }
  }
}
