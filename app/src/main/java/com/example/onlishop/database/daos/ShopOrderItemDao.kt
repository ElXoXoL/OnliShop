package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopBagItem
import com.example.onlishop.database.models.ShopOrderItem
import io.reactivex.rxjava3.core.Completable

@Dao
interface ShopOrderItemDao {

    @Query("SELECT * FROM shoporderitem")
    fun getAll(): List<ShopOrderItem>

    @Query("SELECT * FROM shoporderitem WHERE orderId=:orderId")
    fun loadForOrder(orderId: String): List<ShopOrderItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopOrderItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ShopOrderItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRx(list: List<ShopOrderItem>): Completable

    @Query("SELECT COUNT(id) FROM shoporderitem")
    fun getItemsCount(): Int

    @Query("DELETE FROM shoporderitem")
    fun nukeAll()

    @Query("DELETE FROM shoporderitem")
    fun nukeAllRx(): Completable

}