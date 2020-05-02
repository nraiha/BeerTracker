package com.MobileComputing.BeerTracker

import androidx.room.*

@Entity(tableName = "user")

data class UserInfo(
    @PrimaryKey(autoGenerate = false) var uid: Int?,
    @ColumnInfo(name = "sex") var sex: Int?,
    @ColumnInfo(name = "weight") var weight: Double?
)

@Dao

interface UserDao {
    @Transaction @Insert
    fun insert(user: UserInfo)

    @Query ("SELECT sex FROM user WHERE uid= :id")
    fun getSex(id : Int): Int

    @Query ("SELECT weight FROM user WHERE uid= :id")
    fun getWeight(id : Int): Double

    @Query("SELECT * FROM user WHERE uid= :id")
    fun isUsed(id : Int) : Boolean

    @Query ("SELECT * FROM user")
    fun getAllUsers() : Array<UserInfo>

    @Update
    fun update(userInfo: UserInfo)
}