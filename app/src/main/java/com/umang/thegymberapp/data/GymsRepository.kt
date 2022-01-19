package com.umang.thegymberapp.data

import com.umang.thegymberapp.data.db.GymDb
import com.umang.thegymberapp.data.models.DataResult
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.coroutines.flow.Flow

/**
 * Main entry point to fetch and store gym data
 *
 */
interface GymsRepository {
    /**
     * Get gyms data from api/db
     *
     * @param isRefresh : if true fetch from api else from db
     * @return : Flow object containing either gym data or error
     */
    suspend fun getGyms(isRefresh: Boolean): Flow<DataResult<List<GymDb>>>

    /**
     * to make matched true or false based on right or left swipe on gym
     *
     * @param gymDb :gym db object
     * @param direction :Right or Left swipe direction
     */
    suspend fun matchOrRejectGym(gymDb: GymDb?, direction: Direction?)

    /**
     * To get matched gym list
     *
     * @return : matched gym list
     */
    fun getMatchedGyms(): Flow<List<GymDb>>
}