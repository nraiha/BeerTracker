package com.MobileComputing.BeerTracker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BeerItem::class], version = 2)

abstract class AppDatabase:RoomDatabase()
{
    abstract fun beerDao(): BeerDao
}
