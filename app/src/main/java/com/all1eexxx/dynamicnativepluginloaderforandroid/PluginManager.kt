package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import java.io.File

object PluginManager {
    private const val TAG = "PluginManager"
    private const val PLUGIN_DIR = "plugins"

    fun loadAllPlugins(context: Context) {
        val externalPluginDir = File(context.getExternalFilesDir(null), PLUGIN_DIR)
        val internalPluginDir = File(context.filesDir, PLUGIN_DIR)

        if (!externalPluginDir.exists()) {
            val created = externalPluginDir.mkdirs()
            if (created) {
                Log.i(TAG, "Created external plugin dir: ${externalPluginDir.absolutePath}")
            } else {
                Log.e(TAG, "Failed to create external plugin dir: ${externalPluginDir.absolutePath}")
                return
            }
        }

        TreeCopier.copyPreservingStructure(externalPluginDir, internalPluginDir)

        SoPluginLoader.loadAllSoPlugins(context)
        DexPluginLoader.loadAllDexPlugins(context)
    }

    fun cleanupInternalPlugins(context: Context) {
        val internalPluginDir = File(context.filesDir, PLUGIN_DIR)
        TreeCopier.cleanDirectoryContents(internalPluginDir)
        Log.i(TAG, "Cleaned up internal plugin contents.")
    }
}
