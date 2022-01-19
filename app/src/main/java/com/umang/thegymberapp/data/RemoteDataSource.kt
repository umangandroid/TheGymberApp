package com.umang.thegymberapp.data

import com.umang.thegymberapp.data.models.DataResult
import com.umang.thegymberapp.data.models.GymsResponse

/**
 * To get data from API
 *
 */
abstract class RemoteDataSource {
    /**
     * To get all gyms from API
     *
     * @return : Gym API response
     */
    abstract suspend fun getGyms() : DataResult<GymsResponse>
}