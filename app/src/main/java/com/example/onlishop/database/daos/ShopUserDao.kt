package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopUser

@Dao
interface ShopUserDao {

    @Query("SELECT * FROM shopuser")
    suspend fun getAll(): List<ShopUser>

    @Query("SELECT * FROM shopuser LIMIT 1")
    fun loadSingle(): ShopUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShopUser)

    @Delete
    suspend fun delete(item: ShopUser)

    @Query("DELETE FROM shopuser")
    suspend fun nukeAll()

}