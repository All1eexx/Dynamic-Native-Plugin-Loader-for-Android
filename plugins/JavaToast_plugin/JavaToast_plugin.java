package com.example;

import android.content.Context;
import android.widget.Toast;

public class JavaToast_plugin {
  public static void OnPluginCreate(Context ctx) {
    Toast.makeText(ctx, "Hello from Java Toast Plugin!", Toast.LENGTH_LONG).show();
  }
}
