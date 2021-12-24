package com.vk.superapp.platform

import android.app.Application

class VkDevApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        VkDevUtils.initSuperAppKit(this)
    }
}
