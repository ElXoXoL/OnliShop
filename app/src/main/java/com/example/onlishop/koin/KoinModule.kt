package com.example.onlishop.koin

import androidx.room.Room
import com.example.onlishop.R
import com.example.onlishop.database.AppDatabase
import com.example.onlishop.global.Logger
import com.example.onlishop.global.LoggerImpl
import com.example.onlishop.global.PreferenceRepository
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.groups.GroupsServiceImpl
import com.example.onlishop.network.items.ItemsService
import com.example.onlishop.network.items.ItemsServiceImpl
import com.example.onlishop.repository.ItemRepository
import com.example.onlishop.repository.ItemRepositoryImpl
import com.example.onlishop.repository.OrderRepository
import com.example.onlishop.repository.OrderRepositoryImpl
import com.example.onlishop.ui.detail.DetailsViewModel
import com.example.onlishop.ui.shop.ShopViewModel
import com.example.onlishop.ui.shop.bag.BagViewModel
import com.example.onlishop.ui.shop.order.OrderViewModel
import com.example.onlishop.ui.shop.search.SearchViewModel
import com.example.onlishop.ui.user.UserViewModel
import com.example.onlishop.utils.Crypt
import com.example.onlishop.utils.CryptImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named("ExternalScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    single(named("ExternalOrderScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single <Logger>{ LoggerImpl() }
    single <Crypt>{ CryptImpl("someKey", "abcdefgh") }

    single { PreferenceRepository(get()) }
    single<GroupsService> { GroupsServiceImpl() }
    single<ItemsService> { ItemsServiceImpl() }
    single<ItemRepository> {
        ItemRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single<OrderRepository> {
        OrderRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }


    viewModel { ShopViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { BagViewModel(get(), get()) }
    viewModel { OrderViewModel(get(), get(), get()) }
    viewModel { UserViewModel(get(), get()) }


    viewModel { (itemId: Int) ->
        DetailsViewModel(get(), get(), itemId)
    }

}

val roomModule = module {
    single<AppDatabase>{
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "onli_db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().itemDao() }
    single { get<AppDatabase>().groupDao() }
    single { get<AppDatabase>().bagDao() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().orderDao() }
    single { get<AppDatabase>().orderItemDao() }
}

val roomModuleTest = module(override = true) {
    single<AppDatabase> {
        Room.inMemoryDatabaseBuilder(
            get(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }
    single { get<AppDatabase>().itemDao() }
    single { get<AppDatabase>().groupDao() }
    single { get<AppDatabase>().bagDao() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().orderDao() }
    single { get<AppDatabase>().orderItemDao() }
}

val app = listOf(roomModule, appModule)
val appTest = listOf(roomModuleTest, appModule)