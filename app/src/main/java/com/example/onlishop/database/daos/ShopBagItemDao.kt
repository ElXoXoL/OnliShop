package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopBagItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface ShopBagItemDao {

    @Query("SELECT * FROM ShopBagItem")
    fun getAll(): List<ShopBagItem>

    @Query("SELECT * FROM ShopBagItem")
    fun getAllRx(): Flowable<List<ShopBagItem>>

    @Query("SELECT * FROM ShopBagItem WHERE id=:id AND size=:size")
    fun loadSingle(id: Int, size: String): ShopBagItem?

    @Query("SELECT * FROM ShopBagItem WHERE id=:id AND size=:size")
    fun loadSingleRx(id: Int, size: String): Maybe<ShopBagItem>

    @Query("SELECT * FROM ShopBagItem WHERE bagItemId=:bagItemId")
    fun loadSingle(bagItemId: Int): ShopBagItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopBagItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRx(item: ShopBagItem): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ShopBagItem>)

    @Query("SELECT COUNT(id) FROM ShopBagItem")
    fun getItemsCount(): Int

    @Query("DELETE FROM ShopBagItem WHERE bagItemId=:bagItemId")
    fun delete(bagItemId: Int)

    @Query("DELETE FROM ShopBagItem WHERE bagItemId=:bagItemId")
    fun deleteRx(bagItemId: Int): Completable

    @Update
    fun update(item: ShopBagItem): Completable

    @Query("DELETE FROM ShopBagItem")
    fun nukeAll(): Completable

}