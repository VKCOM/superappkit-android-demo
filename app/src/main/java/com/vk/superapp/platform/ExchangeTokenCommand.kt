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
package com.vk.superapp.platform

import com.vk.auth.main.SilentAuthSource
import com.vk.auth.main.VkFastLoginModifiedUser
import com.vk.auth.main.VkSilentTokenExchanger
import com.vk.silentauth.SilentAuthInfo
import com.vk.superapp.api.core.SuperappApiCore
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class VkSilentTokenExchangerImpl : VkSilentTokenExchanger {
    override fun exchangeSilentToken(
        user: SilentAuthInfo, modifiedUser: VkFastLoginModifiedUser?, source: SilentAuthSource
    ): VkSilentTokenExchanger.Result {
        return try {
            val response = callOnYourBackend(user.token, user.uuid)
            VkSilentTokenExchanger.Result.Success(response.getString("access_token"), response.getLong("user_id"))
        } catch (error: Exception) {
            VkSilentTokenExchanger.Result.Error(error, "Network error!", error !is IOException)
        }
    }

    /**
     * Warning!
     * The following code should be placed on your Backend.
     *
     * Method auth.exchangeSilentAuthToken should be called ONLY from your Backend
     * with Service token from platform.vk.com.
     *
     * For more info, check documentation - https://platform.vk.com/?p=DocsDashboard&docs=tokens_access-token
     */
    private fun callOnYourBackend(
        silentToken: String,
        uuid: String
    ): JSONObject {
        val yourEndpoint = "https://api.vk.com/method/auth.exchangeSilentAuthToken"
        val yourServiceToken = SuperappApiCore.apiManager.anonymTokenManager.provide()
        val postQuery = "access_token=${yourServiceToken}&token=${silentToken}&uuid=${uuid}&v=5.131"
        val request = Request.Builder()
            .url(yourEndpoint)
            .post(postQuery.toRequestBody())
            .build()
        return JSONObject(OkHttpClient().newCall(request).execute().body!!.string())
            .getJSONObject("response")
    }
}