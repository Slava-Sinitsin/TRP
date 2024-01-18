package com.example.trp.domain.di

import android.content.Context
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserDataManager(@ApplicationContext context: Context): UserDataManager {
        return UserDataManager(context = context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataManager: UserDataManager): UserAPIRepositoryImpl {
        return UserAPIRepositoryImpl(userDataManager = userDataManager)
    }
}