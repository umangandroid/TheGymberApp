package com.umang.thegymberapp.data.models

/**
 * API response location object
 *
 * @property addition
 * @property city
 * @property house_number
 * @property id
 * @property latitude
 * @property longitude
 * @property meeting_notes
 * @property street_name
 * @property zip_code
 */
data class Location(
    val addition: String,
    val city: String,
    val house_number: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val meeting_notes: Any,
    val street_name: String,
    val zip_code: String
)