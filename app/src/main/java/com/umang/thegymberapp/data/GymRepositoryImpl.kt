package com.umang.thegymberapp.data

import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.data.models.Gym
import com.umang.thegymberapp.data.db.GymDb
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Implementor of [GymsRepository]
 *
 * @property remoteDataSource  : remote data source to get data from API
 * @property localDataSource : to get data from local db
 */
class GymRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : GymsRepository {


    override suspend fun getGyms(isRefresh: Boolean): Flow<DataResult<List<GymDb>>> {
        return flow {
            if (isRefresh || localDataSource.isNoGymsSaved()) { //make api call if no data in db or refresh call
                val result = remoteDataSource.getGyms()
                if (result is DataResult.Success) {
                    //apply matched gyms ids to fresh data fetched from API
                    val likedGymIds = localDataSource.getLikedGymIds()
                    localDataSource.insertAllGyms(parseResults(result.data.data))
                    localDataSource.likeGyms(likedGymIds)
                    emit(DataResult.Success(localDataSource.getGyms().shuffled()))
                } else {
                    emit(result as DataResult.Error)
                }
            } else {
                emit(DataResult.Success(localDataSource.getGyms().shuffled()))
            }

        }.flowOn(Dispatchers.IO)
    }

    override suspend fun matchOrRejectGym(gymDb: GymDb?, direction: Direction?) {
        direction?.let {
            gymDb?.matched = it == Direction.Right
        }
        localDataSource.updateGymModel(gymDb)
    }

    override fun getMatchedGyms(): Flow<List<GymDb>> {
        return flow {
            emit(localDataSource.getMatchedGyms())
        }.flowOn(Dispatchers.Default)

    }

    /**
     * Get required data from GymResponse and create Gym Db object
     *
     * @param data
     * @return
     */
    fun parseResults(data: List<Gym>): List<GymDb> {
        val gymList = ArrayList<GymDb>()
        data.forEach {
            gymList.add(
                GymDb(
                    id = it.id,
                    latitude = it.locations[0].latitude,
                    longitude = it.locations[0].longitude,
                    name = it.name,
                    url = it.header_image.mobile,
                    address = "${it.locations[0].street_name} ${it.locations[0].city} ${it.locations[0].zip_code}",
                    matched = false
                )
            )
        }
        return gymList

    }

}