package com.dermatoai.repository

import com.dermatoai.api.DermatoBackendEndpoint
import com.dermatoai.api.Diskusi
import com.dermatoai.api.GeneralResponse
import com.dermatoai.api.ListDiskusiResponse
import com.dermatoai.api.TambahDiskusiResponse
import com.dermatoai.api.TambahKomentarRequest
import com.dermatoai.api.TambahKomentarResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DiscussionRepository @Inject constructor(
    private val apiService: DermatoBackendEndpoint,
) {

    suspend fun addDiscussion(
        judul: RequestBody,
        isi: RequestBody,
        kategori: RequestBody,
        idPengguna: RequestBody,
        file: MultipartBody.Part?
    ): Result<TambahDiskusiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.tambahDiskusi(judul, isi, kategori, idPengguna, file)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteDiscussion(id: String): Result<GeneralResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.hapusDiskusi(id)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun addComment(request: TambahKomentarRequest): Result<TambahKomentarResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.tambahKomentar(request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteComment(id: String): Result<GeneralResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.hapusKomentar(id)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getListDiscussion(): Result<ListDiskusiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.listDiskusi()
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getListDiscussionUser(): Result<List<Diskusi>> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.listDiskusiByUser(FirebaseAuth.getInstance().uid.orEmpty())
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getDiscussionById(id: String): Result<Diskusi> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDiskusi(id)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getComments(id: String): Result<Diskusi> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDiskusi(id)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}