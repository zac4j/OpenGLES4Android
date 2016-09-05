package com.zac4j.opengl.util;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Read glsl resources
 * Created by zac on 16-9-5.
 */
public class TextResourceReader {

  /**
   * Read text from ./res/raw file
   * @param context context
   * @param resId resource id
   * @return resource contain text
   */
  public static String readTextFileFromResource(Context context, int resId) {
    StringBuilder sb = new StringBuilder();

    InputStream is = context.getResources().openRawResource(resId);
    InputStreamReader reader = new InputStreamReader(is);
    BufferedReader bufferedReader = new BufferedReader(reader);

    String nextLine;

    try {
      while((nextLine = bufferedReader.readLine()) != null) {
        sb.append(nextLine)
            .append('\n');
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not open resource: " + resId, e);
    }

    return sb.toString();
  }

}
