package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"

        init {
            System.loadLibrary("plugin_loader")
        }

        @JvmStatic
        external fun loadPlugin(context: Context, path: String): Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pluginDir = File(filesDir, "plugins")
        if (!pluginDir.exists()) {
            Log.i(TAG, "The plugin directory does not exist: ${pluginDir.absolutePath}")
            return
        }

        val soFiles = pluginDir.listFiles { _, name -> name.endsWith(".so") }
        if (soFiles.isNullOrEmpty()) {
            Log.i(TAG, "No .so files found in ${pluginDir.absolutePath}")
        } else {
            Log.i(TAG, "Plugins found: ${soFiles.size}")
            for (file in soFiles) {
                val path = file.absolutePath
                Log.i(TAG, "Loading plugin: $path")
                val success = loadPlugin(this, path)
                if (success) {
                    Log.i(TAG, "Plugin loaded successfully: ${file.name}")
                } else {
                    Log.w(TAG, "Failed to load or entry point is missing: ${file.name}")
                }
            }
        }
    }
}
