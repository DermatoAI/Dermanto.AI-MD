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
        AppointmentRecord::class
    ], version = 2, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DermatoDatabase : RoomDatabase() {
    abstract fun diagnoseRecordDao(): DiagnoseRecordDAO
    abstract fun appointmentRecordDao(): AppointmentRecordDAO
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
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
    fun add(newDiagnoseRecord: DiagnoseRecord)

    @Query("select * from appointments where user_id_ref = :userid")
    fun getAll(userid: String): Flow<List<AppointmentRecord>>

    @Query("select * from appointments where user_id_ref = :userid and time > :now order by time limit 1")
    fun getUpcoming(userid: String, now: Date): Flow<AppointmentRecord>

    @Query("delete from appointments where id = :id and user_id_ref = :userid and time > :now")
    fun delete(userid: String, id: String, now: Date): Int
}