package com.umang.thegymberapp.data

import com.umang.thegymberapp.data.db.GymDb

/**
 * Main entry point for db operations on gym data
 *
 */
interface LocalDataSource {

    /**
     * To get all gyms stored in db
     *
     * @return
     */
    fun getGyms() :List<GymDb>

    /**
     * To delete gym data
     *
     */
    fun deleteAllGyms()

    /**
     * To insert all gyms in db
     *
     * @param list : list of gyms
     */
    suspend fun insertAllGyms(list :List<GymDb>)

    /**
     * To check if no data stored in db
     *
     * @return : true if no data in db
     */
    fun isNoGymsSaved(): Boolean

    /**
     * To update Gym db object after match occurred
     *
     * @param gymModel
     */
    suspend fun updateGymModel(gymModel: GymDb?)

    /**
     * To get matched gyms list
     *
     * @return : matched gym list
     */
    fun getMatchedGyms() :List<GymDb>

    /**
     * To get liked gym ids
     *
     * @return :gym ids
     */
    fun getLikedGymIds() :List<Int>

    /**
     * To mark gym matched after fresh data fetched from API
     *
     * @param likedGymIds
     */
    suspend fun likeGyms(likedGymIds: List<Int>)

}