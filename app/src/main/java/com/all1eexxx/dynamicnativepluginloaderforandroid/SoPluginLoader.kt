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

    fun loadAllSoPlugins(context: Context): List<String> {
        val internalPluginDir = File(context.filesDir, "plugins")

        val soFiles = internalPluginDir.walkTopDown()
            .filter { it.isFile && it.extension == "so" }
            .toList()

        val loadedPlugins = mutableListOf<String>()

        if (soFiles.isEmpty()) {
            Log.i(TAG, "No .so plugins found.")
            return loadedPlugins
        }

        for (file in soFiles) {
            val success = loadPlugin(context, file.absolutePath)
            if (success) {
                Log.i(TAG, "Loaded native plugin: ${file.name}")
                loadedPlugins.add(file.name)
            } else {
                Log.w(TAG, "Failed to load native plugin: ${file.name}")
            }
        }

        return loadedPlugins
    }

}