package com.umang.thegymberapp.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Access string resources stored in strings.xml
 *
 * @property appContext
 */
class DataResourceProvider @Inject constructor(@ApplicationContext val appContext : Context) {

    fun getString(resId :Int) :String{
        return appContext.getString(resId)
    }


}