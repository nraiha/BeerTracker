package com.MobileComputing.BeerTracker

import androidx.room.*

@Entity(tableName = "user")

data class UserInfo(
    @PrimaryKey var uid: Int?,
    @ColumnInfo(name = "weight") var weight: Double?,
    @ColumnInfo(name = "sex") var sex: Int?
)

@Dao

interface UserDao {
    @Transaction @Insert
    fun insert(user: UserInfo): Long

    @Query ("SELECT sex FROM user WHERE uid=null")
    fun getSex(): Int

    @Query ("SELECT weight FROM user WHERE uid=null")
    fun getWeight(): Double

    @Update
    fun update(userInfo: UserInfo)
}