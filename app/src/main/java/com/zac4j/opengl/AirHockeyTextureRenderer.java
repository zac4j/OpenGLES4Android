package com.zac4j.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.zac4j.opengl.object.OldMallet;
import com.zac4j.opengl.object.Table;
import com.zac4j.opengl.program.ColorShaderProgram;
import com.zac4j.opengl.program.TextureShaderProgram;
import com.zac4j.opengl.util.MatrixHelper;
import com.zac4j.opengl.util.TextureHelper;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * 纹理渲染器
 * Created by zac on 16-9-8.
 */
public class AirHockeyTextureRenderer implements GLSurfaceView.Renderer {

  private final Context mContext;

  // 投影矩阵容器
  private final float[] mProjectionMatrix = new float[16];

  // 转换矩阵容器
  private final float[] mModelMatrix = new float[16];

  private Table mTable;
  private OldMallet mMallet;

  private TextureShaderProgram mTextureProgram;
  private ColorShaderProgram mColorProgram;

  private int mTexture;

  public AirHockeyTextureRenderer(Context context) {
    mContext = context;
  }

  @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    mTable = new Table();
    mMallet = new OldMallet();

    mTextureProgram = new TextureShaderProgram(mContext);
    mColorProgram = new ColorShaderProgram(mContext);

    mTexture = TextureHelper.loadTexture(mContext, R.drawable.table_texel);
  }

  @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
    glViewport(0, 0, width, height);

    MatrixHelper.perspectiveM(mProjectionMatrix, 45, (float) width / (float) height, 1f, 10f);

    setIdentityM(mModelMatrix, 0);
    translateM(mModelMatrix, 0, 0f, 0f, -2.5f);
    rotateM(mModelMatrix, 0, -60f, 1f, 0f, 0f);

    final float[] temp = new float[16];
    multiplyMM(temp, 0, mProjectionMatrix, 0, mModelMatrix, 0);
    System.arraycopy(temp, 0, mProjectionMatrix, 0, temp.length);
  }

  @Override public void onDrawFrame(GL10 gl10) {
    // Clear the rendering surface
    glClear(GL_COLOR_BUFFER_BIT);

    // 绘制 table
    mTextureProgram.useProgram();
    mTextureProgram.setUniforms(mProjectionMatrix, mTexture);
    mTable.bindData(mTextureProgram);
    mTable.draw();

    // 绘制 mallet
    mColorProgram.useProgram();
    mColorProgram.setUniform(mProjectionMatrix);
    mMallet.bindData(mColorProgram);
    mMallet.draw();
  }
}
