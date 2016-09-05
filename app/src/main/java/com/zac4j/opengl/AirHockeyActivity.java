package com.zac4j.opengl;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    final boolean supportEs3 = configInfo.reqGlEsVersion >= 0x20000;

    if (supportEs3) {
      // Request an OpenGL ES 2.0 compatible context.
      mGLSurfaceView.setEGLContextClientVersion(2);

      // Assign renderer. 设置渲染器
      mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
      mRendererSet = true;
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
