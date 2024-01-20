package com.example.trp.domain.di

import android.app.Application
import androidx.room.Room
import com.example.trp.data.maindb.MainDB
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
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
    fun provideUserRepository(
        mainDB: MainDB
    ): UserAPIRepositoryImpl {
        return UserAPIRepositoryImpl(mainDB = mainDB)
    }
}