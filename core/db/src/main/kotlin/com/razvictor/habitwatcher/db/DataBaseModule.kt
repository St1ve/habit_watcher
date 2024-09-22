package com.razvictor.habitwatcher.db

import android.content.Context
import androidx.room.Room
import com.razvictor.habitwatcher.db.dao.HabitDao
import com.razvictor.habitwathcer.di.qualifiers.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataBaseModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideAppDataBase(@AppContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideHabitDao(appDatabase: AppDatabase): HabitDao {
        return appDatabase.habitDao()
    }
}
