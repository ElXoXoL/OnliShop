package com.example.onlishop.ui.shop.bag

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.onlishop.MainCoroutineRule
import com.example.onlishop.MainCoroutineRuleUnconfined
import com.example.onlishop.TestConstants.GROUPS
import com.example.onlishop.TestConstants.GROUP_1
import com.example.onlishop.TestConstants.GROUP_2
import com.example.onlishop.TestConstants.ITEMS
import com.example.onlishop.TestConstants.ITEM_1
import com.example.onlishop.TestConstants.ITEM_2
import com.example.onlishop.TestConstants.S_GROUPS
import com.example.onlishop.TestConstants.S_ITEMS
import com.example.onlishop.database.AppDatabase
import com.example.onlishop.getOrAwaitValue
import com.example.onlishop.koin.roomModuleTest
import com.example.onlishop.models.BagItem
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.items.ItemsService
import com.example.onlishop.observeForTesting
import com.example.onlishop.repository.ItemRepository
import com.example.onlishop.repository.ItemRepositoryImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import kotlinx.coroutines.yield
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BagViewModelTest : KoinTest {

    companion object {

    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }
    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // region helper fields

    private lateinit var itemsServiceMock: ItemsService
    private lateinit var groupsServiceMock: GroupsService

    // endregion helper fields

    private lateinit var SUT: BagViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        itemsServiceMock = declareMock()
        groupsServiceMock = declareMock()

        whenever(itemsServiceMock.getItems()).thenReturn(S_ITEMS)
        whenever(groupsServiceMock.getGroups()).thenReturn(S_GROUPS)

        loadKoinModules(roomModuleTest)
        SUT = BagViewModel(
            get(),
            get(),
        )
    }

    @After
    fun tearDown() {
        unloadKoinModules(roomModuleTest)
    }

    @Test
    fun fullPriceAndItemsCount_addSomeItems_rightCalculation() = runTest {
        // Arrange

//        SUT.fullPrice.observeForTesting {
//
//            SUT.addItem(ITEM_1, "l")
//            SUT.addItem(ITEM_1, "l")
//            yield()
//
//            assertEquals(2200 * 2, SUT.fullPrice.getOrAwaitValue())
//            assertEquals(2, SUT.itemsInOrderCount)
//
//        }

    }

//    @Test
//    fun another() = runTest {
//
////        assertEquals(2, timesTriggered)
////        assertEquals(
////            listOf(
////                BagItem(
////                    bagItemId = 1,
////                    item = ITEM_1,
////                    size = "l",
////                    count = 1
////                )
////            ), bagItems
////        )
//
//        SUT.addItem(ITEM_1, "l")
//        yield()
//
////        assertEquals(3, timesTriggered)
//        assertEquals(2200 * 2, fullPrice)
//        assertEquals(2, SUT.itemsInOrderCount)
//        assertEquals(
//            listOf(
//                BagItem(
//                    bagItemId = 1,
//                    item = ITEM_1,
//                    size = "l",
//                    count = 2
//                )
//            ), bagItems
//        )
//
//        SUT.addItem(ITEM_1, "xl")
//        yield()
//        assertEquals(2200 * 3, fullPrice)
//        assertEquals(3, SUT.itemsInOrderCount)
//        assertEquals(
//            listOf(
//                BagItem(
//                    bagItemId = 1,
//                    item = ITEM_1,
//                    size = "l",
//                    count = 2
//                ),
//                BagItem(
//                    bagItemId = 2,
//                    item = ITEM_1,
//                    size = "xl",
//                    count = 1
//                ),
//            ), bagItems
//        )
//
//        SUT.removeItem(1)
//        yield()
//        assertEquals(2200 * 2, fullPrice)
//        assertEquals(2, SUT.itemsInOrderCount)
//        assertEquals(
//            listOf(
//                BagItem(
//                    bagItemId = 1,
//                    item = ITEM_1,
//                    size = "l",
//                    count = 1
//                ),
//                BagItem(
//                    bagItemId = 2,
//                    item = ITEM_1,
//                    size = "xl",
//                    count = 1
//                ),
//            ), bagItems
//        )
//
//        SUT.removeItem(1)
//        yield()
//        assertEquals(2200, fullPrice)
//        assertEquals(1, SUT.itemsInOrderCount)
//        assertEquals(
//            listOf(
//                BagItem(
//                    bagItemId = 2,
//                    item = ITEM_1,
//                    size = "xl",
//                    count = 1
//                ),
//            ), bagItems
//        )
//
//        SUT.removeItem(2)
//        yield()
//        assertEquals(0, fullPrice)
//        assertEquals(0, SUT.itemsInOrderCount)
//        assertEquals(emptyList<BagItem>(), bagItems)
//    }

    // region helper methods

    // endregion helper methods

}