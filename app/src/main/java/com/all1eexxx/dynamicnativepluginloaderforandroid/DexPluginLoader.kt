package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.File

object DexPluginLoader {
    private const val TAG = "DexPluginLoader"

    fun loadAllDexPlugins(context: Context): List<String> {
        val sourceDexDir = File(context.getExternalFilesDir(null), "plugins")
        val internalDexDir = File(context.codeCacheDir, "plugins")

        TreeCopier.copyPreservingStructure(sourceDexDir, internalDexDir)

        val dexFiles = internalDexDir.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".dex") }
            .toList()

        val loadedPlugins = mutableListOf<String>()

        if (dexFiles.isEmpty()) {
            Log.i(TAG, "No .dex plugins found.")
            return loadedPlugins
        }

        val optimizedDir = File(context.codeCacheDir, "dex_opt")
        if (!optimizedDir.exists()) {
            optimizedDir.mkdirs()
        }

        for (dexFile in dexFiles) {
            try {
                val readonlyDexFile = File(context.codeCacheDir, dexFile.name)
                dexFile.copyTo(readonlyDexFile, overwrite = true)
                readonlyDexFile.setReadOnly()

                val classLoader = DexClassLoader(
                    readonlyDexFile.absolutePath,
                    optimizedDir.absolutePath,
                    null,
                    context.classLoader
                )

                val pluginClass = classLoader.loadClass("com.example.JavaToast_plugin")
                val method = pluginClass.getDeclaredMethod("OnPluginCreate", Context::class.java)
                method.invoke(null, context)

                Log.i(TAG, "Executed OnPluginCreate from ${dexFile.name}")
                loadedPlugins.add(dexFile.name)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load dex plugin: ${dexFile.name}", e)
            }
        }

        return loadedPlugins
    }

}