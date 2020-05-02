package com.MobileComputing.BeerTracker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BeerItem::class], version = 1)

abstract class AppDatabase:RoomDatabase()
{
    abstract fun beerDao(): BeerDao
}
