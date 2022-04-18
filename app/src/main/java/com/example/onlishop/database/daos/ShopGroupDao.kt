package com.example.onlishop.database.daos

import androidx.room.*
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface ShopGroupDao {

    @Query("SELECT * FROM shopgroup")
    fun getAll(): List<ShopGroup>

    @Query("SELECT * FROM shopgroup")
    fun getAllRx(): Single<List<ShopGroup>>

    @Query("SELECT * FROM shopgroup WHERE id=:id")
    fun loadSingle(id: Int): ShopGroup?

    @Query("SELECT * FROM shopgroup WHERE parentGroupId=:id")
    fun loadChildren(id: Int): List<ShopGroup>

    @Query("SELECT * FROM shopgroup WHERE name LIKE '%' || :search || '%'")
    fun loadForSearchName(search: String): List<ShopGroup>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShopGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ShopGroup>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRx(list: List<ShopGroup>): Completable

    @Query("SELECT COUNT(id) FROM shopgroup")
    fun getItemsCount(): Int

    @Query("SELECT COUNT(id) FROM shopgroup")
    fun getItemsCountRx(): Single<Int>

    @Delete
    fun delete(item: ShopGroup)

    @Update
    fun update(item: ShopGroup)

    @Query("DELETE FROM shopgroup")
    fun nukeAll()

}