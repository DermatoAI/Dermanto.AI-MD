package com.dermatoai.repository

import android.net.Uri
import com.dermatoai.api.DermatoEndpoint
import com.dermatoai.helper.networkBoundResource
import com.dermatoai.room.DiagnoseRecord
import com.dermatoai.room.DiagnoseRecordDAO
import javax.inject.Inject

class AnalyzeRepository @Inject constructor(
    private val dao: DiagnoseRecordDAO,
    private val fetch: DermatoEndpoint,
) {
    fun analyzeImage(uri: Uri, userid: String) = networkBoundResource(
        query = {
            dao.getLatest(userid)
        },
        fetch = {
            fetch.analyzeImage()
        },
        saveFetchResult = { response ->
            response.result.apply {
                dao.add(
                    DiagnoseRecord(
                        0,
                        confidence,
                        diagnosis,
                        timestamp,
                        image,
                        treatmentSuggestions,
                        userid
                    )
                )
            }
        }
    )

    fun getAllAnalyzeRecord(userid: String) = dao.getAll(userid)
}