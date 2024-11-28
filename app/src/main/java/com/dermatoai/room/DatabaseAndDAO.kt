package com.dermatoai.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        DiagnoseRecord::class,
        AppointmentRecord::class
    ], version = 1, exportSchema = false
)
abstract class DermatoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun diagnoseRecordDao(): DiagnoseRecordDAO
    abstract fun appointmentRecordDao(): AppointmentRecordDAO
}

@Dao
interface UserDAO {
    @Insert
    fun add(newUser: User)
}

@Dao
interface DiagnoseRecordDAO {
    @Insert
    fun add(newDiagnoseRecord: DiagnoseRecord)
}

@Dao
interface AppointmentRecordDAO {
    @Insert
    fun addA(newDiagnoseRecord: DiagnoseRecord)
}