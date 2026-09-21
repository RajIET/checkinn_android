package com.example.checkinn_android.domain.usecase

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadIdProofUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(id: Int, imageId: Int): Flow<Resource<ByteArray>> {
        return repository.downloadIdProof(id, imageId)
    }
}
