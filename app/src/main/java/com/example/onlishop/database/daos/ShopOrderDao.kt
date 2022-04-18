package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder
import com.example.onlishop.database.models.ShopUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ShopOrderDao {

    @Query("SELECT * FROM shoporder")
    fun getAll(): List<ShopOrder>

    @Query("SELECT * FROM shoporder")
    fun getAllRx(): Single<List<ShopOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopOrder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRx(item: ShopOrder): Completable

    @Delete
    fun delete(item: ShopOrder)

    @Query("DELETE FROM shoporder")
    fun nukeAll()

    @Query("DELETE FROM shoporder")
    fun nukeAllRx(): Completable

}