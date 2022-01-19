package com.umang.thegymberapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room database for this app
 */
@Database(
    entities = [GymDb::class
    ],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gymDao(): GymDao


}
