package com.example.network

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object GeminiHelper {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generatePassageNotes(
        departure: String,
        destination: String,
        passengerName: String,
        seatNumber: String,
        waveHeight: String,
        windSpeed: String,
        isConcession: Boolean,
        isEnglish: Boolean
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY_DEFAULT_VALUE" || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext if (isEnglish) {
                "Error: Please configure your GEMINI_API_KEY in the AI Studio Secrets panel."
            } else {
                "錯誤：請在 AI Studio 的 Secrets 面板中設定您的 GEMINI_API_KEY。"
            }
        }

        val prompt = if (isEnglish) {
            """
            You are an experienced naval safety expert and tour concierge for the "Blue Ocean Ferry Line" in Taiwan.
            The passenger '$passengerName' has a ticket from '$departure' to '$destination', sitting in seat '$seatNumber'.
            Concession ticket: ${if (isConcession) "Yes" else "No"}.
            Current marine meteorological observations:
            Wave Height: $waveHeight
            Wind Speed: $windSpeed
            
            Please provide a friendly, professional 3-sentence briefing for this voyage.
            Include:
            1. An encouraging, nautical greeting with safety stars reference.
            2. Any weather/sea sickness tips based on waves ($waveHeight) and wind ($windSpeed).
            3. A short highlight of the destination or a cheerful 'Bon Voyage'!
            Keep it under 150 words. Format with exciting marine emojis!
            """.trimIndent()
        } else {
            """
            您是台灣「藍海客輪航線」經驗豐富的航安海事專家與貼心導航員。
            旅客「$passengerName」預訂了從「$departure」到「$destination」的航程，客艙座位為「$seatNumber」號。
            是否為優待票：${if (isConcession) "是" else "否"}。
            今日海象觀測資料：
            浪高：$waveHeight
            風速：$windSpeed
            
            請為這趟航程撰寫一份溫馨、專業、又帶有海洋風情、長度約 3 句的短評與貼心提醒：
            1. 給旅客的航安祝福與元氣問候。
            2. 針對目前的浪高 ($waveHeight) 與風速 ($windSpeed) 給予搭船防暈、乘船安全或舒適度指引。
            3. 對目的地景色稍作提及並祝旅途愉快。
            請控制在 150 字內，並適當加入海洋/客船相關表情符號。
            """.trimIndent()
        }

        try {
            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    return@withContext if (isEnglish) "Network Error: Code ${response.code}\n$errBody" else "連線錯誤：代碼 ${response.code}\n$errBody"
                }

                val resBody = response.body?.string() ?: return@withContext if (isEnglish) "Empty response received." else "接收到空的回應。"
                
                val jsonResponse = JSONObject(resBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val content = candidates.getJSONObject(0).optJSONObject("content")
                    if (content != null) {
                        val parts = content.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text")
                        }
                    }
                }
                if (isEnglish) "Unable to compile advice from Gemini." else "無法編譯來自 Gemini 的建議。"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (isEnglish) "Exception occurred: ${e.localizedMessage}" else "發生異常：${e.localizedMessage}"
        }
    }
}
