package com.MobileComputing.BeerTracker

import androidx.room.*
import java.time.LocalDateTime

@Entity(tableName = "beers")

data class BeerItem(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "beer_name") var beer_name: String?,
    @ColumnInfo(name = "percentage") var percentage: Float?,
    @ColumnInfo(name = "time") var time: Long?,
    @ColumnInfo(name = "location") var location: String?
)

@Dao

interface BeerDao {
    @Transaction @Insert
    fun insert(beers: BeerItem): Long

    @Query ("DELETE FROM beers WHERE uid= :id")
    fun delete (id : Int)

    @Query ("Select * FROM beers")
    fun getBeers(): Array<BeerItem>
}