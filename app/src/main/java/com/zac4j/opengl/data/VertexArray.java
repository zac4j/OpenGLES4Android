package com.zac4j.opengl.data;

import com.zac4j.opengl.Constants;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Encapsulate the FloatBuffer storing the vertex array
 * Created by zac on 16-9-8.
 */
public class VertexArray {

  // FloatBuffer 将传入的 float 数组数据保存到 Native 层
  private final FloatBuffer mFloatBuffer;

  /**
   * 将传入的顶点数据保存到 Native 层
   * @param vertexData 顶点数据
   */
  public VertexArray(float[] vertexData) {
    mFloatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData);
  }

  /**
   * 设置顶点属性
   * @param dataOffset 数据在数组中的偏移量
   * @param attributeLocation 属性定位
   * @param componentCount 属性所需顶点的总数，如二维空间内一个点需要两个浮点数表示一个坐标
   * @param stride 步长
   */
  public void setVertexAttributePointer(int dataOffset, int attributeLocation, int componentCount,
      int stride) {
    // 起始读取位置
    mFloatBuffer.position(dataOffset);
    glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, mFloatBuffer);
    glEnableVertexAttribArray(attributeLocation);

    mFloatBuffer.position(0);
  }

}
