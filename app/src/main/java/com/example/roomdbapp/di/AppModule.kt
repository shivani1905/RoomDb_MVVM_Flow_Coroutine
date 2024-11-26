package com.example.roomdbapp.di

import android.content.Context
import android.net.Uri
import androidx.room.Room
import com.example.roomdbapp.data.datasources.Services
import com.example.roomdbapp.db_utils.CommentDatabase
import com.example.roomdbapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun okHttpClient() = OkHttpClient().newBuilder()
        .addInterceptor(
            Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header("accept", "application/json")
                    //.header("Authorization", "Bearer ")
                    .build()
                chain.proceed(request)
            }
        )
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(getURL())
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getURL())
            .build()
    fun getURL(): String {
    val builder = Uri.Builder()
    builder.scheme("https")
            .authority(Constants.BASE_URL)
          /*.appendPath("api")
        .appendEncodedPath("v2/")*/
    return builder.build().toString()
}
    @Provides
    fun provideService(retrofit: Retrofit): Services =
        retrofit.create(Services::class.java)

    @Singleton
    @Provides
    fun provideFakerDB(@ApplicationContext context : Context) : CommentDatabase {
        return Room.databaseBuilder(context.applicationContext, CommentDatabase::class.java,
            "user_database")
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

}