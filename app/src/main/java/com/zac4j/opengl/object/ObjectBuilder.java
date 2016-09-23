package com.zac4j.opengl.object;

import com.zac4j.opengl.util.Geometry;
import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Object Builder Class
 * Created by zac on 16-9-9.
 */
public class ObjectBuilder {

  static interface DrawCommand {
    void draw();
  }

  static class GeneratedData {
    final float[] vertexData;
    final List<DrawCommand> drawList;

    public GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
      this.vertexData = vertexData;
      this.drawList = drawList;
    }
  }

  // 每个顶点需要三个浮点数表示
  private static final int FLOATS_PER_VERTEX = 3;

  // 顶点容器
  private final float[] vertexData;

  // 绘制方法集合
  private final List<DrawCommand> mDrawList = new ArrayList<>();

  // 追踪下一顶点
  private int offset = 0;

  /**
   * ObjectBuilder 构造器
   *
   * @param sizeInVertices 顶点数量
   */
  private ObjectBuilder(int sizeInVertices) {
    vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
  }

  private GeneratedData build() {
    return new GeneratedData(vertexData, mDrawList);
  }

  /**
   * 圆柱上表面的圆由三角扇形构成，三角扇形包含中心的点，圆边上的各点numPoints，以及开始和结束的同一点
   *
   * @param numPoints 圆形面上的点数量
   * @return 绘制圆柱上表面的圆需要的顶点数量
   */
  private static int sizeOfCircleInVertices(int numPoints) {
    return 1 + (numPoints + 1);
  }

  /**
   * 三角形条带
   *
   * @param numPoints 圆形面上的点数量
   * @return 绘制圆柱侧边需要的顶点数量
   */
  private static int sizeOfOpenCylinderInVertices(int numPoints) {
    return (numPoints + 1) * 2;
  }

  /**
   * 创建冰球
   * @param puck 圆柱对象
   * @param numPoints 圆面边缘点的数量
   * @return 冰球对象
   */
  static GeneratedData createPuck(Geometry.Cylinder puck, int numPoints) {
    // 创建冰球所需的顶点总数
    int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);

    ObjectBuilder builder = new ObjectBuilder(size);

    Geometry.Circle puckTop =
        new Geometry.Circle(puck.center.translateY(puck.height / 2.0f), puck.radius);

    builder.appendCircle(puckTop, numPoints);
    builder.appendOpenCylinder(puck, numPoints);

    return builder.build();
  }

  /**
   * 创建棒槌(由2个圆柱构成)
   *
   * @param center 中心的点
   * @param radius 半径
   * @param height 高度
   * @param numPoints 圆面点的数量
   * @return 棒槌的构造数据
   */
  static GeneratedData createMallet(Geometry.Point center, float radius, float height,
      int numPoints) {
    int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;

    ObjectBuilder builder = new ObjectBuilder(size);

    // 底部的圆柱
    float baseHeight = height * 0.25f;
    Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);
    Geometry.Cylinder baseCylinder =
        new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2.0f), radius, baseHeight);
    builder.appendCircle(baseCircle, numPoints);
    builder.appendOpenCylinder(baseCylinder, numPoints);

    // 上部分圆柱
    float handleHeight = height * 0.75f;
    float handleRadius = radius / 3f;
    Geometry.Circle handleCircle =
        new Geometry.Circle(center.translateY(height * 0.5f), handleRadius);
    Geometry.Cylinder handleCylinder =
        new Geometry.Cylinder(handleCircle.center.translateY(-handleHeight / 2.0f), handleRadius,
            handleHeight);
    builder.appendCircle(handleCircle, numPoints);
    builder.appendOpenCylinder(handleCylinder, numPoints);

    return builder.build();
  }

  /**
   *创建圆面绘制数据
   * @param circle 圆面对象
   * @param numPoints 圆面点的数量
   */
  private void appendCircle(Geometry.Circle circle, int numPoints) {

    // 绘制圆面的起始位置
    final int startVertex = offset / FLOATS_PER_VERTEX;
    // 绘制圆面所需要的顶点数
    final int numVertices = sizeOfCircleInVertices(numPoints);

    // Center point of fan 三角扇形的中心坐标
    vertexData[offset++] = circle.center.x;
    vertexData[offset++] = circle.center.y;
    vertexData[offset++] = circle.center.z;

    for (int i = 0; i <= numPoints; i++) {
      float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2.0f);
      // 假设圆位于x-z平面
      vertexData[offset++] = circle.center.x + circle.radius * ((float) Math.cos(angleInRadians));
      vertexData[offset++] = circle.center.y;
      vertexData[offset++] = circle.center.z + circle.radius * ((float) Math.sin(angleInRadians));
    }

    mDrawList.add(new DrawCommand() {
      @Override public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
      }
    });
  }

  /**
   * 创建圆柱绘制数据
   * @param cylinder 圆柱对象
   * @param numPoints 圆面点的数量
   */
  private void appendOpenCylinder(Geometry.Cylinder cylinder, int numPoints) {

    // 绘制圆柱的起始位置
    final int startVertex = offset / FLOATS_PER_VERTEX;
    // 绘制圆柱所需要的顶点数
    final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
    // 圆柱的底
    final float yStart = cylinder.center.y - (cylinder.height / 2.0f);
    // 圆柱的高
    final float yEnd = cylinder.center.y + (cylinder.height / 2.0f);

    for (int i = 0; i < numPoints; i++) {
      float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2.0f);

      // 假设圆面位于x-z平面
      float xPosition = cylinder.center.x + cylinder.radius * ((float) Math.cos(angleInRadians));
      float zPosition = cylinder.center.z + cylinder.radius * ((float) Math.sin(angleInRadians));

      vertexData[offset++] = xPosition;
      vertexData[offset++] = yStart;
      vertexData[offset++] = zPosition;

      vertexData[offset++] = xPosition;
      vertexData[offset++] = yEnd;
      vertexData[offset++] = zPosition;
    }

    mDrawList.add(new DrawCommand() {
      @Override public void draw() {
        glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
      }
    });
  }
}
