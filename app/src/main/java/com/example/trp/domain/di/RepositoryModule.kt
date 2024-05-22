package com.example.trp.domain.di

import android.app.Application
import androidx.room.Room
import com.example.trp.data.maindb.MainDB
import com.example.trp.data.network.UserAPI
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideUserDB(app: Application): MainDB {
        return Room.databaseBuilder(
            context = app,
            klass = MainDB::class.java,
            name = "User.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideApiService(): UserAPI {
        val interceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://87.103.245.211:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        mainDB: MainDB,
        apiService: UserAPI
    ): UserAPIRepositoryImpl {
        return UserAPIRepositoryImpl(mainDB = mainDB, apiService = apiService)
    }
}