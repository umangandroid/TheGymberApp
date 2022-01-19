package com.umang.thegymberapp.data.models

/**
 * API response gym image object
 *
 * @property desktop
 * @property hdpi
 * @property ldpi
 * @property mdpi
 * @property mobile
 * @property orig
 * @property tablet
 * @property thumbnail
 * @property xhdpi
 * @property xxhdpi
 * @property xxxhdpi
 */
data class HeaderImage(
    val desktop: String,
    val hdpi: String,
    val ldpi: String,
    val mdpi: String,
    val mobile: String,
    val orig: String,
    val tablet: String,
    val thumbnail: String,
    val xhdpi: String,
    val xxhdpi: String,
    val xxxhdpi: String
)