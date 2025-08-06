package com.all1eexxx.plugix

import android.content.Context
import android.util.Log
import java.io.File

object PluginManager {
    private const val TAG = "PluginManager"
    private const val PLUGIN_DIR = "plugins"

    private val loadedSoPlugins = mutableListOf<String>()
    private val loadedDexPlugins = mutableListOf<String>()

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

        val soLoaded = SoPluginLoader.loadAllSoPlugins(context)
        loadedSoPlugins.addAll(soLoaded)

        val dexLoaded = DexPluginLoader.loadAllDexPlugins(context)
        loadedDexPlugins.addAll(dexLoaded)

        Log.i(TAG, "Total native plugins loaded: ${loadedSoPlugins.size}")
        Log.i(TAG, "Total dex plugins loaded: ${loadedDexPlugins.size}")
    }

    fun cleanupInternalPlugins(context: Context) {
        val internalPluginDir = File(context.filesDir, PLUGIN_DIR)
        TreeCopier.cleanDirectoryContents(internalPluginDir)

        val internalDexDir = File(context.codeCacheDir, PLUGIN_DIR)
        TreeCopier.cleanDirectoryContents(internalDexDir)

        Log.i(TAG, "Cleaned up internal plugin contents.")
    }
}