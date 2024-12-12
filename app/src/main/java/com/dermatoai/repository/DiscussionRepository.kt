package com.dermatoai.repository

import com.dermatoai.api.DermatoEndpoint
import com.dermatoai.api.Diskusi
import com.dermatoai.api.GeneralResponse
import com.dermatoai.api.ListDiskusiResponse
import com.dermatoai.api.Pengguna
import com.dermatoai.api.TambahDiskusiRequest
import com.dermatoai.api.TambahDiskusiResponse
import com.dermatoai.api.TambahKomentarRequest
import com.dermatoai.api.TambahKomentarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DiscussionRepository @Inject constructor(
    private val apiService: DermatoEndpoint,
) {

    suspend fun addDiscussion(request: TambahDiskusiRequest): Result<TambahDiskusiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.tambahDiskusi(request)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteDiscussion(id: Int): Result<GeneralResponse> {
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

    suspend fun deleteComment(id: Int): Result<GeneralResponse> {
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
//                val response = apiService.listDiskusi()
                Result.success(
                    ListDiskusiResponse(
                        status = "lacus", data = listOf(
                            Diskusi(
                                id = 7115,
                                judul = "solum",
                                isi = "legimus",
                                kategori = "aptent",
                                pengguna = Pengguna(id = "maiestatis", username = "Pansy Humphrey"),
                                timestamp = "luptatum",
                                jumlahKomentar = 6717,
                                isFavorite = false,
                                images = emptyList()
                            ),
                            Diskusi(
                                id = 8528,
                                judul = "veri",
                                isi = "sociosqu",
                                kategori = "definitiones",
                                pengguna = Pengguna(id = "sadipscing", username = "Pierre Tate"),
                                timestamp = "eleifend",
                                jumlahKomentar = 9811,
                                isFavorite = false,
                                images = emptyList()
                            )
                        )
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}