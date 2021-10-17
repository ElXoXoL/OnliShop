package com.example.onlishop.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onlishop.database.daos.ShopGroupDao
import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.database.models.ShopGroup
import com.example.onlishop.database.models.ShopItem
import com.example.onlishop.database.models.ShopOrder
import com.example.onlishop.database.models.ShopUser

@Database(entities =
[
    ShopGroup::class,
    ShopItem::class,
    ShopOrder::class,
    ShopUser::class
], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ShopItemDao
    abstract fun groupDao(): ShopGroupDao
//    abstract fun playerDao(): PlayerDao
}