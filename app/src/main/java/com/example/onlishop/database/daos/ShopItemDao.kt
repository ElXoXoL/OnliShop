package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface ShopItemDao {

    @Query("SELECT * FROM shopitem")
    fun getAll(): List<ShopItem>

    @Query("SELECT * FROM shopitem")
    fun getAllRx(): Single<List<ShopItem>>

    @Query("SELECT * FROM shopitem WHERE id=:id")
    fun loadSingle(id: Int): ShopItem?

    @Query("SELECT * FROM shopitem WHERE id=:id")
    fun loadSingleRx(id: Int): Maybe<ShopItem>

    @Query("SELECT * FROM shopitem WHERE groupId=:id")
    fun loadForGroup(id: Int): List<ShopItem>

    @Query("SELECT * FROM shopitem WHERE groupId=:id")
    fun loadForGroupRx(id: Int): Observable<List<ShopItem>>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRx(list: List<ShopItem>)

    @Query("SELECT COUNT(id) FROM shopitem")
    fun getItemsCount(): Int

    @Query("SELECT COUNT(id) FROM shopitem")
    fun getItemsCountRx(): Single<Int>

    @Delete
    fun delete(item: ShopItem)

    @Update
    fun update(item: ShopItem)

    @Query("DELETE FROM shopitem")
    fun nukeAll()

}