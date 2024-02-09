package tinkoff.fintech.lab.di.module

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tinkoff.fintech.lab.data.network.KinopoiskApi
import javax.inject.Singleton

@Module
class NetworkModule {

    private companion object {
        const val BASE_URL: String = "https://kinopoiskapiunofficial.tech/api/v2.2/films/"
        const val API_KEY: String = "X-API-KEY"
        const val API_VALUE: String = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    }

    @Provides
    @Singleton
    fun getInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient())
        .build()

    @Provides
    @Singleton
    fun authInterceptor(): Interceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader(API_KEY, API_VALUE)
                .build()
        )
    }

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor())
        .addNetworkInterceptor(httpLoggingInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): KinopoiskApi = retrofit.create(KinopoiskApi::class.java)

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}