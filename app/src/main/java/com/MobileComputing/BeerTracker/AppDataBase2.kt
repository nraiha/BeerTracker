package com.MobileComputing.BeerTracker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserInfo::class], version = 2)

abstract class AppDatabase2:RoomDatabase()
{
    abstract fun userDao(): UserDao
}
