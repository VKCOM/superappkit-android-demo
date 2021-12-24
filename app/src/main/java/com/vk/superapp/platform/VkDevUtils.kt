package com.vk.superapp.platform

import android.app.Application
import androidx.appcompat.content.res.AppCompatResources
import com.vk.api.sdk.VK
import com.vk.auth.external.VkExternalServiceAuthMethod
import com.vk.auth.main.*
import com.vk.superapp.SuperappKit
import com.vk.superapp.SuperappKitConfig
import com.vk.superapp.core.SuperappConfig

internal object VkDevUtils {

    fun initSuperAppKit(context: Application) {

        // You should specify these parameters and appId in resource file!
        val appName = context.getString(R.string.app_name)
        val clientSecret = context.getString(R.string.vk_client_secret)

        // Specify an icon which would be shown on UI components
        val icon = AppCompatResources.getDrawable(context, R.mipmap.ic_launcher)!!

        val appInfo = SuperappConfig.AppInfo(
            appName,
            VK.getAppId(context).toString(),
            BuildConfig.VERSION_NAME
        )

        val config = SuperappKitConfig.Builder(context)
            // VK ID settings
            .setAuthModelData(clientSecret)
            .setAuthUiManagerData(VkClientUiInfo(icon, appName))
            .setLegalInfoLinks(
                serviceUserAgreement = "https://id.vk.com/terms",
                servicePrivacyPolicy = "https://id.vk.com/privacy"
            )
            .setApplicationInfo(appInfo)

            // Sets the implementation for exchanging silent token strategy
            // Warning! You should use your own implementation of VkSilentTokenExchanger
            .setSilentTokenExchanger(VkSilentTokenExchangerImpl())

            .build()

        // Initialize Kit
        SuperappKit.init(config)
    }
}