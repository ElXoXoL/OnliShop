package com.example.onlishop.koin

import androidx.room.Room
import com.example.onlishop.database.AppDatabase
import com.example.onlishop.database.RoomDatabase
import com.example.onlishop.global.Logger
import com.example.onlishop.global.PreferenceRepository
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.groups.GroupsServiceImpl
import com.example.onlishop.network.items.ItemsService
import com.example.onlishop.network.items.ItemsServiceImpl
import com.example.onlishop.repository.ItemRepository
import com.example.onlishop.repository.ItemRepositoryImpl
import com.example.onlishop.ui.shop.ShopViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named("ExternalScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    single { Logger() }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "onli_db"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        RoomDatabase(get())
    }

    single { PreferenceRepository(get()) }
    single<GroupsService> { GroupsServiceImpl() }
    single<ItemsService> { ItemsServiceImpl() }
    single<ItemRepository> { ItemRepositoryImpl(get(), get(), get(), get(), get(named("ExternalScope"))) }

//    viewModel {
//        (paymentClient: PaymentClient) -> MainViewModel(get(), get(), get(), paymentClient)
//    }
    viewModel { ShopViewModel(get(), get()) }
//
//    viewModel {
//            (packId: String) -> CardsViewModel(get(), get(), packId)
//    }

}

val app = listOf(appModule)