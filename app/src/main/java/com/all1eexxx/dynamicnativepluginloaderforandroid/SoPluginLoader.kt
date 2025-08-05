package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import java.io.File

object SoPluginLoader {
    private const val TAG = "SoPluginLoader"

    init {
        System.loadLibrary("plugin_loader")
    }

    @JvmStatic
    external fun loadPlugin(context: Context, path: String): Boolean

    fun loadAllSoPlugins(context: Context) {
        val internalPluginDir = File(context.filesDir, "plugins")

        val soFiles = internalPluginDir.walkTopDown()
            .filter { it.isFile && it.extension == "so" }
            .toList()

        if (soFiles.isEmpty()) {
            Log.i(TAG, "No .so plugins found.")
            return
        }

        for (file in soFiles) {
            val success = loadPlugin(context, file.absolutePath)
            if (success) {
                Log.i(TAG, "Loaded native plugin: ${file.name}")
            } else {
                Log.w(TAG, "Failed to load native plugin: ${file.name}")
            }
        }
    }
}
