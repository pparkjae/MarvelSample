package com.example.marvelsample.di

import com.example.marvelsample.BuildConfig
import com.example.marvelsample.Config
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.marvel.data.api.MarvelApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    @Singleton
    @Provides
    fun providesHeaderInterceptor() = Interceptor {
        val ts = System.currentTimeMillis().toString()

        it.proceed(
            it.request().newBuilder().url(
                it.request().url.newBuilder()
                    .addQueryParameter("ts", ts)
                    .addQueryParameter("apikey", "3b69326a106ca9975c7189131bb4cda8")
                    .addQueryParameter(
                        "hash",
                        "$ts${Config.PRIVATE_API_KEY}${Config.PUBLIC_API_KEY}".md5()
                    )
                    .build()
            ).build()
        )
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(interceptor: Interceptor) = OkHttpClient.Builder().apply {
        hostnameVerifier { _, _ -> true }
        addInterceptor(interceptor)
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        connectTimeout(15, TimeUnit.SECONDS)
        writeTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun provideConverterFactory(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
            isLenient = true
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder().apply {
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            baseUrl("https://gateway.marvel.com/")
            client(okHttpClient)
        }.build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MarvelApi =
        retrofit.create(MarvelApi::class.java)
}