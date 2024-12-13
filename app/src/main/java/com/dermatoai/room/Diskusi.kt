package com.dermatoai.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_posts")
data class LikesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "discussionId") val discussionId: String = "",
    @ColumnInfo(name = "judul") val judul: String,
    @ColumnInfo(name = "isi") val isi: String,
    @ColumnInfo(name = "kategori") val kategori: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "createdAt") val timestamp: String?,
    @ColumnInfo(name = "jumlah_komentar") val jumlahKomentar: Int,
    @ColumnInfo(name = "user_id") val userId: String
)
