package com.umang.thegymberapp.data


import com.umang.thegymberapp.data.db.AppDatabase
import com.umang.thegymberapp.data.db.GymDb
import javax.inject.Inject

/**
 * Implementor of [LocalDataSource]
 *
 * @property appDatabase
 */
class LocalDataSourceImpl @Inject constructor(private val appDatabase: AppDatabase) :LocalDataSource {
    override fun getGyms(): List<GymDb> {
        return appDatabase.gymDao().getAllData()
    }

    override fun deleteAllGyms() {
        appDatabase.gymDao().deleteGymsFromDb()
    }

    override suspend fun insertAllGyms(gyms :List<GymDb>) {
        appDatabase.gymDao().insertAll(gyms)
    }

    override fun isNoGymsSaved(): Boolean {
        return appDatabase.gymDao().getGymCountFromDb() == 0
    }

    override suspend fun updateGymModel(gymModel: GymDb?) {
        appDatabase.gymDao().updateGymModel(gymModel)
    }

    override fun getMatchedGyms(): List<GymDb> {
     return appDatabase.gymDao().getMatchedGyms()
    }

    override fun getLikedGymIds(): List<Int> {
     return  appDatabase.gymDao().getMatchedGymIds()
    }

    override suspend fun likeGyms(likedGymIds: List<Int>) {
        appDatabase.gymDao().updateMatchGymIdsToDb(likedGymIds)
    }
}