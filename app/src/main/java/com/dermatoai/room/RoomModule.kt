package com.dermatoai.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DermatoDatabase {
        return Room.databaseBuilder(
            context,
            DermatoDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDiagnoseRecordDao(database: DermatoDatabase): DiagnoseRecordDAO =
        database.diagnoseRecordDao()

    @Provides
    fun provideAppointmentRecordDao(database: DermatoDatabase): AppointmentRecordDAO =
        database.appointmentRecordDao()

}