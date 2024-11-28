package com.dermatoai.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String
)

@Entity(tableName = "diagnose_records")
data class DiagnoseRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "confidence_score")
    val confidenceScore: Int,
    val issue: String,
    val time: Long,
    val image: String,
    @ColumnInfo(name = "additional_info")
    val additionalInfo: String,
    @ColumnInfo(name = "user_id_ref")
    val userId: String
)

@Entity(tableName = "appointments")
data class AppointmentRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Long,
    @ColumnInfo(name = "doctor_name")
    val doctorName: String,
)

