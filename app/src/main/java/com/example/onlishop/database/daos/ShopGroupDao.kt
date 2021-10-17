package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem

@Dao
interface ShopGroupDao {

    @Query("SELECT * FROM shopgroup")
    suspend fun getAll(): List<ShopGroup>

    @Query("SELECT * FROM shopgroup WHERE id=:id")
    fun loadSingle(id: Int): ShopGroup?

    @Query("SELECT * FROM shopgroup WHERE parentGroupId=:id")
    fun loadChildren(id: Int): List<ShopGroup>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShopGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShopGroup>)

    @Query("SELECT COUNT(id) FROM shopgroup")
    fun getItemsCount(): Int

    @Delete
    suspend fun delete(item: ShopGroup)

    @Update
    suspend fun update(item: ShopGroup)

    @Query("DELETE FROM shopgroup")
    suspend fun nukeAll()

}