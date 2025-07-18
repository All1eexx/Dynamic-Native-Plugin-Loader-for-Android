package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException

object PluginManager {
    private const val TAG = "PluginManager"
    private const val INTERNAL_PLUGIN_DIR = "native_plugins"

    init {
        System.loadLibrary("plugin_loader")
    }

    @JvmStatic
    external fun loadPlugin(context: Context, path: String): Boolean

    fun loadAllPlugins(context: Context) {
        val externalPluginDir = File(context.getExternalFilesDir(null), "plugins")
        if (!externalPluginDir.exists()) {
            val created = externalPluginDir.mkdirs()
            if (created) {
                Log.i(TAG, "Created external plugin directory: ${externalPluginDir.absolutePath}")
            } else {
                Log.e(TAG, "Failed to create external plugin directory: ${externalPluginDir.absolutePath}")
                return
            }
        }

        val soFiles = externalPluginDir.listFiles { _, name -> name.endsWith(".so") }
        if (soFiles.isNullOrEmpty()) {
            Log.i(TAG, "No .so files found in ${externalPluginDir.absolutePath}")
            return
        }

        Log.i(TAG, "Plugins found: ${soFiles.size}")

        val internalPluginDir = File(context.filesDir, INTERNAL_PLUGIN_DIR)
        if (!internalPluginDir.exists()) {
            internalPluginDir.mkdirs()
        }

        for (file in soFiles) {
            try {
                val internalFile = copyPluginToInternalStorage(context, file)
                val internalPath = internalFile.absolutePath
                Log.i(TAG, "Loading plugin from internal path: $internalPath")
                val success = loadPlugin(context, internalPath)
                if (success) {
                    Log.i(TAG, "Plugin loaded successfully: ${file.name}")
                } else {
                    Log.w(TAG, "Failed to load or entry point is missing: ${file.name}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error copying plugin: ${file.name}", e)
            }
        }
    }

    private fun copyPluginToInternalStorage(context: Context, externalFile: File): File {
        val internalPluginDir = File(context.filesDir, INTERNAL_PLUGIN_DIR)
        if (!internalPluginDir.exists()) {
            internalPluginDir.mkdirs()
        }

        val targetFile = File(internalPluginDir, externalFile.name)
        externalFile.copyTo(targetFile, overwrite = true)
        return targetFile
    }

    fun cleanupInternalPlugins(context: Context) {
        val internalPluginDir = File(context.filesDir, INTERNAL_PLUGIN_DIR)
        if (internalPluginDir.exists()) {
            val files = internalPluginDir.listFiles()
            if (files != null) {
                for (file in files) {
                    val deleted = file.delete()
                    Log.i(TAG, if (deleted) "Deleted ${file.name}" else "Failed to delete ${file.name}")
                }
            }
        }
    }
}
