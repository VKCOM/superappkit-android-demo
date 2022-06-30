/**
 * Copyright (c) 2020 - present, LLC “V Kontakte”
 *
 * 1. Permission is hereby granted to any person obtaining a copy of this Software to
 * use the Software without charge.
 *
 * 2. Restrictions
 * You may not modify, merge, publish, distribute, sublicense, and/or sell copies,
 * create derivative works based upon the Software or any part thereof.
 *
 * 3. Termination
 * This License is effective until terminated. LLC “V Kontakte” may terminate this
 * License at any time without any negative consequences to our rights.
 * You may terminate this License at any time by deleting the Software and all copies
 * thereof. Upon termination of this license for any reason, you shall continue to be
 * bound by the provisions of Section 2 above.
 * Termination will be without prejudice to any rights LLC “V Kontakte” may have as
 * a result of this agreement.
 *
 * 4. Disclaimer of warranty and liability
 * THE SOFTWARE IS MADE AVAILABLE ON THE “AS IS” BASIS. LLC “V KONTAKTE” DISCLAIMS
 * ALL WARRANTIES THAT THE SOFTWARE MAY BE SUITABLE OR UNSUITABLE FOR ANY SPECIFIC
 * PURPOSES OF USE. LLC “V KONTAKTE” CAN NOT GUARANTEE AND DOES NOT PROMISE ANY
 * SPECIFIC RESULTS OF USE OF THE SOFTWARE.
 * UNDER NO CIRCUMSTANCES LLC “V KONTAKTE” BEAR LIABILITY TO THE LICENSEE OR ANY
 * THIRD PARTIES FOR ANY DAMAGE IN CONNECTION WITH USE OF THE SOFTWARE.
 */

package com.vk.superapp.platform.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.auth.main.VkClientAuthLib
import com.vk.superapp.bridges.superappApi
import com.vk.superapp.platform.R

/**
 * Sample Main Activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        VkClientAuthLib.addAuthCallback(authCallback)
    }

    private val authCallback = object : com.vk.auth.main.VkClientAuthCallback {
        override fun onAuth(authResult: com.vk.auth.api.models.AuthResult) {
            superappApi.users.sendGetUserMyInfo(
                VK.getAppId(
                    this@MainActivity.applicationContext).toLong(),
                0
            ).subscribe {
                val info = it.getJSONArray("response")
                if (info.length() > 0) {
                    val firstUser = info.getJSONObject(0)
                    Toast.makeText(
                        this@MainActivity,
                        "${firstUser.getInt("id")} " +
                                "${firstUser.getString("first_name")} " +
                                firstUser.getString("last_name"),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "No users found!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        VkClientAuthLib.removeAuthCallback(authCallback)
        super.onDestroy()
    }
}
