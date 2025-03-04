package com.prod.bookit.data.repository

import com.prod.bookit.data.mappers.toDomain
import com.prod.bookit.data.remote.api.CoworkingsApi
import com.prod.bookit.domain.model.CoworkingDetail
import com.prod.bookit.domain.model.CoworkingSummary
import com.prod.bookit.domain.repository.CoworkingsRepository

class CoworkingsRepositoryImpl(
    private val api: CoworkingsApi
): CoworkingsRepository {
    override suspend fun getAllCoworkings(): List<CoworkingSummary> {
        return try {
            api.getAllCoworkings().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCoworkingDetail(id: String): CoworkingDetail {
        return api.getCoworkingById(id).toDomain()
    }
}