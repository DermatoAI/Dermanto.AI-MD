package com.dermatoai.repository

import com.dermatoai.api.Diskusi
import com.dermatoai.room.LikesDao
import com.dermatoai.room.LikesEntity
import javax.inject.Inject

class LikesRepository @Inject constructor(private val likesDao: LikesDao) {

    suspend fun addLike(diskusi: Diskusi, userId: String) {
        val like = LikesEntity(
            judul = diskusi.judul,
            isi = diskusi.isi,
            discussionId = diskusi.id,
            kategori = diskusi.kategori,
            username = diskusi.authorId,
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
