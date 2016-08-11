package com.example.michal.rentmate.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtil {

  private Context context;
  private static FontUtil oneInstance = null;

  private FontUtil(Context context) {
    this.context = context.getApplicationContext();
  }

  public static FontUtil getInstance(Context context) {
    if (oneInstance == null) {
      oneInstance = new FontUtil(context);
    }
    return oneInstance;
  }

  public static void setFont(Context context, String fontName,TextView text) {
    Typeface fancyOne = Typeface.createFromAsset(context.getAssets(), fontName);
    text.setTypeface(fancyOne);
  }
}
