package com.umang.thegymberapp.fakedata

import com.umang.thegymberapp.data.LocalDataSource
import com.umang.thegymberapp.data.db.GymDb

class FakeLocalDataSource :LocalDataSource {
    private val fakeRepository = FakeRepository()
    val response = fakeRepository.getFormattedResponse()
    override fun getGyms(): List<GymDb> {
        return fakeRepository.getFormattedResponse()
    }

    override fun deleteAllGyms() {
    }

    override suspend fun insertAllGyms(list: List<GymDb>) {
    }

    override fun isNoGymsSaved(): Boolean {
        return true
    }

    override suspend fun updateGymModel(gymModel: GymDb?) {
        response.find { it.id ==gymModel?.id }?.matched = gymModel!!.matched
    }

    override fun getMatchedGyms(): List<GymDb> {
        return response.filter { it.matched }
    }

    override fun getLikedGymIds(): List<Int> {
        return fakeRepository.getFormattedResponse().map { it.id }
    }

    override suspend fun likeGyms(likedGymIds: List<Int>) {
    }
}