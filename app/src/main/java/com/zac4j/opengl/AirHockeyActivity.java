package com.zac4j.opengl;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class AirHockeyActivity extends AppCompatActivity {

  private GLSurfaceView mGLSurfaceView;

  // 变量 rendererSet 记录 GLSurfaceView 是否可用
  private boolean mRendererSet = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mGLSurfaceView = new GLSurfaceView(this);

    // to check if system is actually supports OpenGL ES 2.0
    // 检查系统是否支持 OpenGL ES 2.0
    final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

    final ConfigurationInfo configInfo = activityManager.getDeviceConfigurationInfo();

    final boolean supportEs2 = configInfo.reqGlEsVersion >= 0x20000;

    if (supportEs2) {
      // Request an OpenGL ES 2.0 compatible context.
      mGLSurfaceView.setEGLContextClientVersion(2);

      // Assign renderer. 设置渲染器
      final AirHockeyCylinderRenderer renderer = new AirHockeyCylinderRenderer(this);

      mGLSurfaceView.setRenderer(renderer);
      mRendererSet = true;

      mGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
          // 将Android 屏幕坐标转换为标准坐标
          if (event != null) {
            final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
            final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
              mGLSurfaceView.queueEvent(new Runnable() {
                @Override public void run() {
                  renderer.onClick(normalizedX, normalizedY);
                }
              });
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
              mGLSurfaceView.queueEvent(new Runnable() {
                @Override public void run() {
                  renderer.onDrag(normalizedX, normalizedY);
                }
              });
            }
            return true;
          }
          return false;
        }
      });
    } else {
      Toast.makeText(AirHockeyActivity.this, "This device does not support OpenGL ES 2.0",
          Toast.LENGTH_SHORT).show();
    }

    setContentView(mGLSurfaceView);
  }

  @Override protected void onPause() {
    super.onPause();
    if (mRendererSet) {
      mGLSurfaceView.onPause();
    }
  }

  @Override protected void onResume() {
    super.onResume();
    if (mRendererSet) {
      mGLSurfaceView.onResume();
    }
  }
}
