package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import java.io.File

object PluginManager {
    private const val TAG = "PluginManager"

    init {
        System.loadLibrary("plugin_loader")
    }

    @JvmStatic
    external fun loadPlugin(context: Context, path: String): Boolean

    fun loadAllPlugins(context: Context) {
        val pluginDir = File(context.filesDir, "plugins")
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
                val success = loadPlugin(context, path)
                if (success) {
                    Log.i(TAG, "Plugin loaded successfully: ${file.name}")
                } else {
                    Log.w(TAG, "Failed to load or entry point is missing: ${file.name}")
                }
            }
        }
    }
}
