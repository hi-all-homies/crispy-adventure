package ru.stanise.animebrowsing.config

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.stanise.animebrowsing.dto.OffsetDateTimeSerializer
import java.io.IOException
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit


class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var lastException: IOException? = null

        while (attempt < maxRetries) {
            try {
                return chain.proceed(chain.request())
            } catch (e: IOException) {
                lastException = e
                attempt++
                Thread.sleep(400)
            }
        }
        throw lastException ?: IOException("Unknown error after $maxRetries attempts")
    }
}


private val contentType = "application/json".toMediaType()

private val timeSerializersModule = SerializersModule {
    contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
}
private val json = Json {
    ignoreUnknownKeys = true
    serializersModule = timeSerializersModule
}


private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(RetryInterceptor())
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .callTimeout(25, TimeUnit.SECONDS)
    .build()


val retrofit: Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(Config.BASE_URL)
    .addConverterFactory(json.asConverterFactory(contentType))
    .build()