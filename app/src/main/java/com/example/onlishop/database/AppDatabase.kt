package com.example.onlishop.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onlishop.database.daos.*
import com.example.onlishop.database.models.*

@Database(entities =
[
    ShopGroup::class,
    ShopItem::class,
    ShopOrder::class,
    ShopUser::class,
    ShopBagItem::class,
    ShopOrderItem::class,
], version = 8
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ShopItemDao
    abstract fun groupDao(): ShopGroupDao
    abstract fun bagDao(): ShopBagItemDao
    abstract fun userDao(): ShopUserDao
    abstract fun orderDao(): ShopOrderDao
    abstract fun orderItemDao(): ShopOrderItemDao
//    abstract fun playerDao(): PlayerDao
}