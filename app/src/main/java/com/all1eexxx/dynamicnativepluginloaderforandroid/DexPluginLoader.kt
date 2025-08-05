package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import dalvik.system.DexFile
import java.io.File
import java.lang.reflect.Modifier

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

                val optimizedDexPath = File(optimizedDir, dexFile.name + ".opt").absolutePath
                @Suppress("DEPRECATION")
                val dexFileObj = DexFile.loadDex(readonlyDexFile.absolutePath, optimizedDexPath, 0)
                @Suppress("DEPRECATION")
                val entries = dexFileObj.entries()

                var foundEntry = false

                while (entries.hasMoreElements()) {
                    val className = entries.nextElement()
                    try {
                        val clazz = classLoader.loadClass(className)

                        val method = clazz.declaredMethods.find {
                            it.name == "OnPluginCreate" &&
                                    it.parameterTypes.size == 1 &&
                                    it.parameterTypes[0] == Context::class.java &&
                                    Modifier.isStatic(it.modifiers)
                        }

                        if (method != null) {
                            method.invoke(null, context)
                            Log.i(TAG, "Executed OnPluginCreate from $className in ${dexFile.name}")
                            loadedPlugins.add(dexFile.name)
                            foundEntry = true
                            break
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Error checking class $className", e)
                    }
                }

                if (!foundEntry) {
                    Log.w(TAG, "No suitable entry point found in ${dexFile.name}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Failed to load dex plugin: ${dexFile.name}", e)
            }
        }
        return loadedPlugins
    }
}