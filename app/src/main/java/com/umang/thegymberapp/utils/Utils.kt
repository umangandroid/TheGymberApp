package com.umang.thegymberapp.utils

import android.location.Location

/**
 * To calculate distance between two location in miles
 *
 * @param location : user location
 * @param latitude : gym latitude
 * @param longitude : gym longitude
 * @return :distance in miles
 */
fun getDistanceBetweenTwoCoordinates(
    location: Location,
    latitude: Double,
    longitude: Double,
): String {
    val location2 = Location("")
    location2.latitude = latitude
    location2.longitude = longitude
    val meters = location.distanceTo(location2)
    val miles  :Double= meters*0.000621371192
    return  String.format("%.1f", miles)
}