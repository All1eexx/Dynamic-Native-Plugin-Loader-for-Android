package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException

object PluginManager {
    private const val TAG = "PluginManager"
    private const val INTERNAL_PLUGIN_DIR = "plugins"

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

        // Копируем всю структуру плагинов
        copyPluginStructureToInternalStorage(context, externalPluginDir)

        // Загружаем только .so файлы
        val soFiles = externalPluginDir.walk()
            .filter { it.isFile && it.extension == "so" }
            .toList()

        if (soFiles.isEmpty()) {
            Log.i(TAG, "No .so files found in ${externalPluginDir.absolutePath}")
            return
        }

        Log.i(TAG, "Plugins found: ${soFiles.size}")

        for (file in soFiles) {
            try {
                // Получаем соответствующий внутренний путь
                val relativePath = file.relativeTo(externalPluginDir).path
                val internalFile = File(File(context.filesDir, INTERNAL_PLUGIN_DIR), relativePath)
                val internalPath = internalFile.absolutePath

                Log.i(TAG, "Loading plugin from internal path: $internalPath")
                val success = loadPlugin(context, internalPath)
                if (success) {
                    Log.i(TAG, "Plugin loaded successfully: ${file.name}")
                } else {
                    Log.w(TAG, "Failed to load or entry point is missing: ${file.name}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error processing plugin: ${file.name}", e)
            }
        }
    }

    private fun copyPluginStructureToInternalStorage(context: Context, externalDir: File) {
        val internalPluginDir = File(context.filesDir, INTERNAL_PLUGIN_DIR)

        // Очищаем старые плагины перед копированием
        cleanupInternalPlugins(context)

        // Копируем всю структуру рекурсивно
        externalDir.walk().forEach { source ->
            try {
                val relativePath = source.relativeTo(externalDir).path
                val dest = File(internalPluginDir, relativePath)

                if (source.isDirectory) {
                    if (!dest.exists()) {
                        dest.mkdirs()
                        Log.d(TAG, "Created directory: ${dest.absolutePath}")
                    }
                } else {
                    source.copyTo(dest, overwrite = true)
                    Log.d(TAG, "Copied file: ${source.name} to ${dest.absolutePath}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error copying ${source.name}", e)
            }
        }
    }

    fun cleanupInternalPlugins(context: Context) {
        val internalPluginDir = File(context.filesDir, INTERNAL_PLUGIN_DIR)
        if (internalPluginDir.exists()) {
            internalPluginDir.deleteRecursively()
            Log.i(TAG, "Cleaned up internal plugin directory")
        }
    }
}