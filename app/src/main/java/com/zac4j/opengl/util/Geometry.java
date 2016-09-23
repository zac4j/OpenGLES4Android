package com.zac4j.opengl.util;

/**
 * 几何图形类
 * Created by zac on 16-9-9.
 */
public class Geometry {

  /**
   * 点
   */
  public static class Point {
    public final float x, y, z;

    public Point(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    /**
     * 沿y轴偏移
     * @param distance y 轴的偏移量
     * @return 偏移后的点
     */
    public Point translateY(float distance) {
      return new Point(x, y + distance, z);
    }

  }

  /**
   * 圆
   */
  public static class Circle {
    public final Point center;
    public final float radius;

    public Circle(Point center, float radius) {
      this.center = center;
      this.radius = radius;
    }

    /**
     * 伸缩圆形
     * @param scale 伸缩的量
     * @return 伸缩后的圆形
     */
    public Circle scale(float scale) {
      return new Circle(center, radius * scale);
    }
  }

  /**
   * 圆柱
   */
  public static class Cylinder {
    public final Point center;
    public final float radius;
    public final float height;

    public Cylinder(Point center, float radius, float height) {
      this.center = center;
      this.radius = radius;
      this.height = height;
    }
  }

}
