package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder
import com.example.onlishop.database.models.ShopUser

@Dao
interface ShopOrderDao {

    @Query("SELECT * FROM shoporder")
    suspend fun getAll(): List<ShopOrder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShopOrder)

    @Delete
    suspend fun delete(item: ShopOrder)

    @Query("DELETE FROM shoporder")
    suspend fun nukeAll()

}