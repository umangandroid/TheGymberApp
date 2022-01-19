package com.umang.thegymberapp.data.models

/**
 * API Response Gym object
 *
 * @property header_image
 * @property id
 * @property locations
 * @property name
 */
data class Gym(
    val header_image: HeaderImage,
    val id: Int,
    val locations: List<Location>,
    val name: String
)