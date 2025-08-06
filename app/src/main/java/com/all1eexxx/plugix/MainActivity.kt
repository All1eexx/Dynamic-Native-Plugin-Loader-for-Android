package com.all1eexxx.plugix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PluginManager.loadAllPlugins(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        PluginManager.cleanupInternalPlugins(this)
    }
}