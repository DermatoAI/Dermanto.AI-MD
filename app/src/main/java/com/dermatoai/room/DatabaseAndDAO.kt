package com.dermatoai.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Database(
    entities = [
        DiagnoseRecord::class,
        AppointmentRecord::class,
        LikesEntity::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DermatoDatabase : RoomDatabase() {
    abstract fun diagnoseRecordDao(): DiagnoseRecordDAO
    abstract fun appointmentRecordDao(): AppointmentRecordDAO
    abstract fun likeDao(): LikesDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

@Dao
interface DiagnoseRecordDAO {
    @Insert
    fun add(newDiagnoseRecord: DiagnoseRecord)

    @Query("select * from diagnoses where user_id_ref = :userid")
    fun getAll(userid: String): Flow<List<DiagnoseRecord>>

    @Query("select * from diagnoses where user_id_ref = :userId order by time desc limit 1")
    fun getLatest(userId: String): Flow<DiagnoseRecord>
}

@Dao
interface AppointmentRecordDAO {
    @Insert
    fun addA(newDiagnoseRecord: DiagnoseRecord)

    @Query("select * from appointments where user_id_ref = :userid")
    fun getAll(userid: String): Flow<List<AppointmentRecord>>
}