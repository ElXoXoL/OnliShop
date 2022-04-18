package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface ShopUserDao {

    @Query("SELECT * FROM shopuser")
    fun getAll(): List<ShopUser>

    @Query("SELECT * FROM shopuser LIMIT 1")
    fun loadSingle(): ShopUser?

    @Query("SELECT * FROM shopuser LIMIT 1")
    fun loadSingleRx(): Maybe<ShopUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopUser): Completable

    @Delete
    fun delete(item: ShopUser)

    @Query("DELETE FROM shopuser")
    fun nukeAll()

    @Query("DELETE FROM shopuser")
    fun nukeAllRx(): Completable

}