package com.jch.main

import android.app.Application
import com.jch.plugin.PluginTools

/**
 *
 * @author changhua.jiang
 * @since 2018/2/27 下午3:22
 */
class KotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PluginTools.init(this)
    }
}