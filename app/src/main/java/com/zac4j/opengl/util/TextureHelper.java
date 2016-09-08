package com.zac4j.opengl.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Texture utility class help to load image file data
 * Created by zac on 16-9-7.
 */
public class TextureHelper {

  private static final String TAG = "TextureHelper";

  public static int loadTexture(Context context, int resourceId) {
    final int[] textureObjectIds = new int[1];
    // 生成1个 texture 对象引用到 textureObjectIds，偏移量为0.
    glGenTextures(1, textureObjectIds, 0);

    if (textureObjectIds[0] == 0) {
      if (LoggerConfig.ON) {
        Log.w(TAG, "Could not generate a new OpenGL texture object.");
      }
      return 0;
    }

    // 借助 BitmapFactory 加载未经压缩的图片文件(OpenGL无法直接加载未经压缩的PNG、JPEG文件)
    final BitmapFactory.Options options = new BitmapFactory.Options();
    // 设置为未伸缩类型，即原图
    options.inScaled = false;

    final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

    if (bitmap == null) {
      if (LoggerConfig.ON) {
        Log.w(TAG, "Resource ID " + resourceId + "could not be decoded.");
      }

      // 移除 texture 对象
      glDeleteTextures(1, textureObjectIds, 0);
      return 0;
    }

    // 绑定纹理对象
    glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

    // 对缩小纹理使用纹理映射三线性插值算法
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

    // 对放大纹理使用双线性插值算法
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    // 向 OpenGL中载入 bitmap by GLUtils.texImage2D(...)
    texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

    // 回收 bitmap
    bitmap.recycle();

    // 生成纹理映射
    glGenerateMipmap(GL_TEXTURE_2D);

    // a good practise --> 解绑纹理
    glBindTexture(GL_TEXTURE_2D, 0);

    return textureObjectIds[0];
  }

}
