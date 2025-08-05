package com.all1eexxx.dynamicnativepluginloaderforandroid

import android.util.Log
import java.io.File
import java.io.IOException

object TreeCopier {
    private const val TAG = "TreeCopier"

    fun copyPreservingStructure(sourceRoot: File, targetRoot: File) {
        if (!sourceRoot.exists()) {
            Log.w(TAG, "Source root does not exist: ${sourceRoot.absolutePath}")
            return
        }

        if (!targetRoot.exists()) {
            targetRoot.mkdirs()
            Log.d(TAG, "Created target root: ${targetRoot.absolutePath}")
        }

        val existingPaths = sourceRoot.walk().map { it.relativeTo(sourceRoot).path }.toSet()
        targetRoot.walkTopDown().forEach { target ->
            val relative = target.relativeTo(targetRoot).path
            if (relative.isNotEmpty() && relative !in existingPaths) {
                if (target.isDirectory) {
                    target.deleteRecursively()
                    Log.d(TAG, "Deleted obsolete directory: ${target.absolutePath}")
                } else {
                    target.delete()
                    Log.d(TAG, "Deleted obsolete file: ${target.absolutePath}")
                }
            }
        }

        sourceRoot.walk().forEach { source ->
            try {
                val relativePath = source.relativeTo(sourceRoot).path
                val dest = File(targetRoot, relativePath)

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
                Log.e(TAG, "Error copying ${source.absolutePath}", e)
            }
        }
    }

    fun cleanDirectoryContents(dir: File) {
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.forEach {
                if (it.isDirectory) {
                    it.deleteRecursively()
                } else {
                    it.delete()
                }
            }
        }
    }
}
