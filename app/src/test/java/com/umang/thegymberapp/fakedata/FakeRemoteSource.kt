package com.umang.thegymberapp.fakedata

import com.umang.thegymberapp.data.RemoteDataSource
import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.data.models.GymsResponse

class FakeRemoteSource : RemoteDataSource() {
    private val fakeRepository = FakeRepository()
    override suspend fun getGyms(): DataResult<GymsResponse> {
        return DataResult.Success(fakeRepository.getResponse())
    }
}