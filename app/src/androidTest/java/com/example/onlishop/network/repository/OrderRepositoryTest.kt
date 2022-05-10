package com.example.onlishop.network.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.onlishop.MainCoroutineRuleUnconfined
import com.example.onlishop.TestConstants.S_GROUPS
import com.example.onlishop.TestConstants.S_ITEMS
import com.example.onlishop.TestConstants.ORDER_1
import com.example.onlishop.TestConstants.ORDER_2
import com.example.onlishop.TestConstants.S_ITEM_1
import com.example.onlishop.TestConstants.S_ITEM_2
import com.example.onlishop.TestConstants.USER
import com.example.onlishop.database.AppDatabase
import com.example.onlishop.database.daos.ShopItemDao
import com.example.onlishop.koin.roomModuleTest
import com.example.onlishop.models.Order
import com.example.onlishop.models.User
import com.example.onlishop.repository.OrderRepository
import com.example.onlishop.repository.OrderRepositoryImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class OrderRepositoryTest : KoinTest {

    companion object {

    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }
    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleUnconfined()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // region helper fields

    // endregion helper fields

    private lateinit var SUT: OrderRepository

    private lateinit var itemDaoMock: ShopItemDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loadKoinModules(roomModuleTest)

        itemDaoMock = declareMock()

        whenever(itemDaoMock.loadSingle(1)).thenReturn(S_ITEM_1)
        whenever(itemDaoMock.loadSingle(2)).thenReturn(S_ITEM_2)

        SUT = OrderRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    @After
    fun tearDown() {
        unloadKoinModules(roomModuleTest)
    }

    @Test
    fun getUserFlow_addUserAndRemove_rightBehaviour() = runTest {
        // Arrange
        val resultList = mutableListOf<User?>()
        SUT.getUserFlow().asLiveData().observeForever {
            resultList.add(it)
        }

        // Should receive null on connect

        SUT.addUser(USER)

        // Should receive USER after add

        SUT.removeUser()

        // Should receive null after removing

        assertEquals(
            listOf(null, USER, null),
            resultList
        )
    }

    @Test
    fun getOrdersFlow_addTwoOrders_receiveNullThenOrderThenBoth() = runTest {
        // Arrange
        var resultList = listOf<Order>()
        SUT.getOrdersFlow().asLiveData().observeForever {
            resultList = it
        }

        assertEquals(emptyList<Order>(), resultList)

        SUT.addOrder(ORDER_1)

        assertEquals(listOf(ORDER_1), resultList)

        SUT.addOrder(ORDER_2)

        assertEquals(listOf(ORDER_1, ORDER_2), resultList)

    }

    @Test
    fun getLastOrder_addTwoOrders_receiveNullThenOrderThenBoth() = runTest {
        // Arrange

        assertEquals(null, SUT.getLastOrder())

        SUT.addOrder(ORDER_1)

        assertEquals(ORDER_1, SUT.getLastOrder())

        SUT.addOrder(ORDER_2)

        assertEquals(ORDER_2, SUT.getLastOrder())

    }

    // region helper methods

    // endregion helper methods

}