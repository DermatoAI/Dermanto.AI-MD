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
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Date,
    @ColumnInfo(name = "doctor_name")
    val doctorName: String,
    @ColumnInfo(name = "user_id_ref")
    val userId: String
)

