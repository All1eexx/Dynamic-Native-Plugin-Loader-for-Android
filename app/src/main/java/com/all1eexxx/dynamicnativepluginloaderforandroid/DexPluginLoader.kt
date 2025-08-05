package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.File

object DexPluginLoader {
    private const val TAG = "DexPluginLoader"

    fun loadAllDexPlugins(context: Context) {
        val internalPluginDir = File(context.filesDir, "plugins")
        val dexFiles = internalPluginDir.walkTopDown()
            .filter { it.isFile && it.extension == "dex" }
            .toList()

        if (dexFiles.isEmpty()) {
            Log.i(TAG, "No .dex plugins found.")
            return
        }

        val optimizedDir = File(context.codeCacheDir, "dex_opt")
        if (!optimizedDir.exists()) {
            optimizedDir.mkdirs()
        }

        for (dexFile in dexFiles) {
            try {
                val classLoader = DexClassLoader(
                    dexFile.absolutePath,
                    optimizedDir.absolutePath,
                    null,
                    context.classLoader
                )

                val pluginClass = classLoader.loadClass("OnPluginCreate")
                val method = pluginClass.getDeclaredMethod("invoke", Context::class.java)
                method.invoke(null, context)

                Log.i(TAG, "Executed OnPluginCreate from ${dexFile.name}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load dex plugin: ${dexFile.name}", e)
            }
        }
    }
}
