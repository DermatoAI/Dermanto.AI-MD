package com.dermatoai.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "diagnoses")
data class DiagnoseRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "confidence_score")
    val confidenceScore: Int,
    val issue: String,
    val time: Date,
    val image: String,
    @ColumnInfo(name = "additional_info")
    val additionalInfo: String,
    @ColumnInfo(name = "user_id_ref")
    val userId: String
)

@Entity(tableName = "appointments")
data class AppointmentRecord(
    @PrimaryKey
    val id: String,
    val time: Date,
    @ColumnInfo(name = "doctor_id_ref")
    val doctorId: String,
    @ColumnInfo(name = "doctor_name")
    val doctorName: String,
    val location: String,
    @ColumnInfo(name = "user_id_ref")
    val userId: String
)

