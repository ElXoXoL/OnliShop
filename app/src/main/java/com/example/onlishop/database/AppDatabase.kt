package com.example.onlishop.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onlishop.database.daos.ShopBagItemDao
import com.example.onlishop.database.daos.ShopGroupDao
import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.database.models.*

@Database(entities =
[
    ShopGroup::class,
    ShopItem::class,
    ShopOrder::class,
    ShopUser::class,
    ShopBagItem::class
], version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ShopItemDao
    abstract fun groupDao(): ShopGroupDao
    abstract fun bagDao(): ShopBagItemDao
//    abstract fun playerDao(): PlayerDao
}