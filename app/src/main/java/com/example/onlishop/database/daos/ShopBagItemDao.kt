package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopBagItem

@Dao
interface ShopBagItemDao {

    @Query("SELECT * FROM ShopBagItem")
    suspend fun getAll(): List<ShopBagItem>

    @Query("SELECT * FROM ShopBagItem WHERE id=:id AND size=:size")
    fun loadSingle(id: Int, size: String): ShopBagItem?

    @Query("SELECT * FROM ShopBagItem WHERE bagItemId=:bagItemId")
    fun loadSingle(bagItemId: Int): ShopBagItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShopBagItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShopBagItem>)

    @Query("SELECT COUNT(id) FROM ShopBagItem")
    fun getItemsCount(): Int

    @Query("DELETE FROM ShopBagItem WHERE bagItemId=:bagItemId")
    suspend fun delete(bagItemId: Int)

    @Update
    suspend fun update(item: ShopBagItem)

    @Query("DELETE FROM ShopBagItem")
    suspend fun nukeAll()

}