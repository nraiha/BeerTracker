package com.MobileComputing.BeerTracker

import androidx.room.*

@Entity(tableName = "user")

data class UserInfo(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "weight") var weight: Double?,
    @ColumnInfo(name = "sex") var sex: Int?
)

@Dao

interface UserDao {
    @Transaction @Insert
    fun insert(user: UserInfo): Long

    @Query ("SELECT sex FROM user")
    fun getSex(): Int

    @Query ("SELECT weight FROM user")
    fun getWeight(): Double

    @Update
    fun update(userInfo: UserInfo)
}