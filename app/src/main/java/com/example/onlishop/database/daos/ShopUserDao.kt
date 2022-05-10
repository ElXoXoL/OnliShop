package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopUser
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopUserDao {

    @Query("SELECT * FROM shopuser")
    fun getAll(): List<ShopUser>

    @Query("SELECT * FROM shopuser")
    fun getAllFlow(): Flow<List<ShopUser>>

    @Query("SELECT * FROM shopuser LIMIT 1")
    fun loadSingle(): ShopUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopUser)

    @Delete
    fun delete(item: ShopUser)

    @Query("DELETE FROM shopuser")
    fun nukeAll()

}