package com.zac4j.opengl.util;

/**
 * Matrix utility class to helper create Perspective Matrix
 * Created by zac on 16-9-7.
 */
public class MatrixHelper {

  /**
   * 创建透视矩阵
   * @param m 矩阵数据容器
   * @param yFovInDegrees 视域的角度 [0, Math.PI]
   * @param aspect 屏幕纵横比
   * @param n 近视平面距离
   * @param f 远视平面距离
   */
  public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
    // 计算焦距(focal length) a
    final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0f);
    final float a = (float) (1.0f / Math.tan(angleInRadians / 2.0f));

    // 填充矩阵的数据 运算公式为: http://od497o5qc.bkt.clouddn.com/aspectWithFieldVision.png
    m[0] = a / aspect;
    m[1] = 0f;
    m[2] = 0f;
    m[3] = 0f;

    m[4] = 0f;
    m[5] = a;
    m[6] = 0f;
    m[7] = 0f;

    m[8] = 0f;
    m[9] = 0f;
    m[10] = -((f + n) / (f - n));
    m[11] = -1f;

    m[12] = 0f;
    m[13] = 0f;
    m[14] = -((2f * f * n) / (f - n));
    m[15] = 0f;
  }

}
