package com.umang.thegymberapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Used to store gym data received from API to Db object
 *
 * @property id : gym id
 * @property distance : distance mile from current location
 * @property latitude : gym latitude
 * @property longitude :gym longitude
 * @property name : gym name
 * @property url :gym picture url
 * @property address : gym address formatted
 * @property matched : true when gym matched
 */
@Entity
data class GymDb(
    @PrimaryKey
    val id: Int,
    var distance :String = "",
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val url :String,
    val address: String,
    var matched :Boolean
)
