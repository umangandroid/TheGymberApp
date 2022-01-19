package com.umang.thegymberapp.data.db

import androidx.room.*

/**
 * Contains queries to operate on data stored for gyms
 *
 */
@Dao
interface GymDao {
    @Query("select * from GymDb")
    fun getAllData(): List<GymDb> //get all gyms stored in db

    @Query("select * from GymDb where matched =1")
    fun getMatchedGyms(): List<GymDb> //get matched gys

    @Query("select id from GymDb where matched =1")
    fun getMatchedGymIds(): List<Int> // get matched gym ids

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<GymDb>) // add all gyms to db

    @Query("delete from GymDb")
    fun deleteGymsFromDb() // to delete gyms from db

    @Query("select count(id) from GymDb")
    fun getGymCountFromDb(): Int // get gyms count stored in db, used to decide first call to make

    @Update
    suspend fun updateGymModel(gymModel: GymDb?) //update gym model after match occurred

    @Query("update GymDb set matched=1 where id IN (:values)")
    suspend fun updateMatchGymIdsToDb(values :List<Int>)

}