package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem

@Dao
interface ShopItemDao {

    @Query("SELECT * FROM shopitem")
    suspend fun getAll(): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE id=:id")
    fun loadSingle(id: Int): ShopItem?

    @Query("SELECT * FROM shopitem WHERE groupId=:id")
    fun loadForGroup(id: Int): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE name LIKE '%' || :search || '%'")
    fun loadForSearch(search: String): List<ShopItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShopItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShopItem>)

    @Query("SELECT COUNT(id) FROM shopitem")
    fun getItemsCount(): Int

    @Delete
    suspend fun delete(item: ShopItem)

    @Update
    suspend fun update(item: ShopItem)

    @Query("DELETE FROM shopitem")
    suspend fun nukeAll()

}