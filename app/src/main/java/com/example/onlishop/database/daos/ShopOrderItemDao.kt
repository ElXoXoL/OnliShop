package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopBagItem
import com.example.onlishop.database.models.ShopOrderItem

@Dao
interface ShopOrderItemDao {

    @Query("SELECT * FROM shoporderitem")
    suspend fun getAll(): List<ShopOrderItem>

    @Query("SELECT * FROM shoporderitem WHERE orderId=:orderId")
    fun loadForOrder(orderId: String): List<ShopOrderItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShopOrderItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShopOrderItem>)

    @Query("SELECT COUNT(id) FROM shoporderitem")
    fun getItemsCount(): Int

    @Query("DELETE FROM shoporderitem")
    suspend fun nukeAll()

}