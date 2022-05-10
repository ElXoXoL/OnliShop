package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem

@Dao
interface ShopItemDao {

    @Query("SELECT * FROM shopitem")
    fun getAll(): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE id=:id")
    fun loadSingle(id: Int): ShopItem?

    @Query("SELECT * FROM shopitem WHERE groupId=:id")
    fun loadForGroup(id: Int): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE name LIKE '%' || :search || '%'")
    fun loadForSearchName(search: String): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE description LIKE '%' || :search || '%'")
    fun loadForSearchDescr(search: String): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE sizes LIKE '%' || :search || '%'")
    fun loadForSearchSizes(search: String): List<ShopItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ShopItem>)

    @Query("SELECT COUNT(id) FROM shopitem")
    fun getItemsCount(): Int

    @Delete
    fun delete(item: ShopItem)

    @Update
    fun update(item: ShopItem)

    @Query("DELETE FROM shopitem")
    fun nukeAll()

}