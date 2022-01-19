package com.umang.thegymberapp.fakedata

import com.google.gson.Gson
import com.umang.thegymberapp.data.*
import com.umang.thegymberapp.data.db.GymDb
import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.data.models.GymsResponse
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.mockito.kotlin.mock

class FakeRepository : GymsRepository {
    private val gymsRepositoryImpl :GymRepositoryImpl = GymRepositoryImpl(
        mock<RemoteDataSourceImpl>(),
        mock<LocalDataSourceImpl>()
    )
    override suspend fun getGyms(isRefresh: Boolean): Flow<DataResult<List<GymDb>>> {
        return flow {
            emit(getGymResults())
        }.flowOn(Dispatchers.IO)
    }

    private fun getGymResults(): DataResult<List<GymDb>> {
        return DataResult.Success(getFormattedResponse())
    }

     fun getFormattedResponse(): List<GymDb> {

           return gymsRepositoryImpl.parseResults(getResponse().data)
    }

    fun getResponse() :GymsResponse{
        return Gson().fromJson(FAKE_RESPONSE,GymsResponse::class.java)
    }

    override suspend fun matchOrRejectGym(gymDb: GymDb?, direction: Direction?) {
        TODO("Not yet implemented")
    }

    override fun getMatchedGyms(): Flow<List<GymDb>> {
        TODO("Not yet implemented")
    }
}