package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder
import com.example.onlishop.database.models.ShopUser
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopOrderDao {

    @Query("SELECT * FROM shoporder")
    fun getAllFlow(): Flow<List<ShopOrder>>

    @Query("SELECT * FROM shoporder")
    fun getAll(): List<ShopOrder>

    @Query("SELECT * FROM shoporder ORDER BY autoId DESC LIMIT 1")
    fun getLast(): ShopOrder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopOrder)

    @Delete
    fun delete(item: ShopOrder)

    @Query("DELETE FROM shoporder")
    fun nukeAll()

}