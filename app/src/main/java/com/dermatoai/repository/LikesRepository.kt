package com.dermatoai.repository

import com.dermatoai.api.Diskusi
import com.dermatoai.room.LikesDao
import com.dermatoai.room.LikesEntity
import javax.inject.Inject

class LikesRepository @Inject constructor(private val likesDao: LikesDao) {

    suspend fun addLike(diskusi: Diskusi, userId: String) {
        val like = LikesEntity(
            id = diskusi.id,
            judul = diskusi.judul,
            isi = diskusi.isi,
            kategori = diskusi.kategori,
            penggunaId = diskusi.pengguna.id,
            username = diskusi.pengguna.username,
            timestamp = diskusi.timestamp,
            jumlahKomentar = diskusi.jumlahKomentar,
            userId = userId
        )
        likesDao.addLike(like)
    }

    suspend fun removeLike(likesEntity: LikesEntity) {
        likesDao.removeLike(likesEntity)
    }

    suspend fun getLikedPosts(userId: String): List<LikesEntity> {
        return likesDao.getLikedPosts(userId)
    }

    suspend fun isPostLiked(diskusiId: Int, userId: String): LikesEntity? {
        return likesDao.isPostLiked(diskusiId, userId)
    }
}
