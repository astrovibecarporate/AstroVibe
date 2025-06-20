package com.example.astrovibe.di

import android.content.Context
import com.example.astrovibe.data.remote.AstrologerApi
import com.example.astrovibe.data.remote.UserApiService
import com.example.astrovibe.data.repo.AstrologerRepo
import com.example.astrovibe.data.repo.AstrologerRepoImpl
import com.example.astrovibe.data.repo.UserRepo
import com.example.astrovibe.data.repo.UserRepoImpl
import com.example.astrovibe.data.utils.AppPreferences
import com.example.astrovibe.data.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val httpLoggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
//        val certificatePin = ""
//
//        val certificatePinner = CertificatePinner.Builder()
//            .add("com.thetopheadlines",certificatePin) // host name
//            .build()

        val httpClient = OkHttpClient.Builder()
            //           .certificatePinner(certificatePinner)
            .addInterceptor(httpLoggingInterceptor)  // Logs API requests
            .addInterceptor(headerInterceptor)  // Adds authentication headers
            .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)  // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)  // Write timeout
            .retryOnConnectionFailure(true)  // Retries on failure
            .build()

        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }


    @Provides
    @Singleton
    fun provideAstrologerApi(retrofit: Retrofit): AstrologerApi =
        retrofit.create(AstrologerApi::class.java)

    @Provides
    @Singleton
    fun provideAstrologerRepo(api: AstrologerApi): AstrologerRepo = AstrologerRepoImpl(api)


    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideUserRepo(api: UserApiService): UserRepo = UserRepoImpl(api)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }




}