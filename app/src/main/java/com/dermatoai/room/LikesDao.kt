package com.dermatoai.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikesDao {

    @Insert
    suspend fun addLike(like: LikesEntity)

    @Delete
    suspend fun removeLike(like: LikesEntity)

    @Query("SELECT * FROM liked_posts WHERE user_id = :userId")
    suspend fun getLikedPosts(userId: String): List<LikesEntity>

    @Query("SELECT * FROM liked_posts WHERE id = :diskusiId AND user_id = :userId")
    suspend fun isPostLiked(diskusiId: Int, userId: String): LikesEntity?
}
