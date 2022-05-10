package com.example.onlishop.network.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
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
import com.example.onlishop.koin.roomModuleTest
import com.example.onlishop.models.BagItem
import com.example.onlishop.network.groups.GroupsService
import com.example.onlishop.network.items.ItemsService
import com.example.onlishop.repository.ItemRepository
import com.example.onlishop.repository.ItemRepositoryImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ItemRepositoryTest : KoinTest {

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

    private lateinit var itemsServiceMock: ItemsService
    private lateinit var groupsServiceMock: GroupsService

    // endregion helper fields

    private lateinit var SUT: ItemRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        itemsServiceMock = declareMock()
        groupsServiceMock = declareMock()

        whenever(itemsServiceMock.getItems()).thenReturn(S_ITEMS)
        whenever(groupsServiceMock.getGroups()).thenReturn(S_GROUPS)

        loadKoinModules(roomModuleTest)
        SUT = ItemRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    @After
    fun tearDown() {
        unloadKoinModules(roomModuleTest)
    }

    @Test
    fun getItemsAndGroups_firstTime_fromService() = runTest {

        val items = SUT.getItems()
        val groups = SUT.getGroups()

        assertEquals(
            ITEMS,
            items
        )

        assertEquals(
            GROUPS,
            groups
        )

        verify(itemsServiceMock).getItems()
        verify(groupsServiceMock).getGroups()
    }

    @Test
    fun getItemsAndGroups_secondTime_fromDatabase() = runTest {
        val items = SUT.getItems()
        val groups = SUT.getGroups()

        SUT.getItems()
        SUT.getGroups()

        assertEquals(
            ITEMS,
            items
        )

        assertEquals(
            GROUPS,
            groups
        )

        verify(itemsServiceMock, times(1)).getItems()
        verify(groupsServiceMock, times(1)).getGroups()
    }

    @Test
    fun getGroupChildrenAndParent_rightData() = runTest {
        val groupsForId = SUT.getGroupChildrenAndParent(11)

        assertEquals(
            listOf(GROUP_1, GROUP_2),
            groupsForId
        )
    }

    @Test
    fun getItemsAndSubitemsForGroup_rightData() = runTest {
        val itemsForGroup = SUT.getItemsAndSubitemsForGroup(1)

        assertEquals(
            listOf(ITEM_1, ITEM_2),
            itemsForGroup
        )
    }

    @Test
    fun getItems_rightData() = runTest {
        val item = SUT.getItem(2)

        assertEquals(
            ITEM_2,
            item
        )
    }

    @Test
    fun getSearchItems_rightData() = runTest {
        val searchByX = SUT.getSearchItems("x")

        assertEquals(
            ITEMS,
            searchByX
        )

        val searchByXX = SUT.getSearchItems("xx")

        assertEquals(
            listOf(ITEM_2),
            searchByXX
        )
    }

    @Test
    fun getBagItemsFlowAndSize_addSomeItemsAndRemove_rightBehaviour() = runTest {
        // Arrange
        var bagSize = 0
        var bagItems = listOf<BagItem>()
        SUT.getBagSizeFlow().asLiveData().observeForever {
            bagSize = it
        }
        SUT.getBagItemsFlow().asLiveData().observeForever {
            bagItems = it
        }

        // Act, Assert
        assertEquals(0, bagSize)
        assertEquals(emptyList<BagItem>(), bagItems)

        SUT.addBagItem(ITEM_1, "l")
        assertEquals(1, bagSize)
        assertEquals(
            listOf(
                BagItem(
                    bagItemId = 1,
                    item = ITEM_1,
                    size = "l",
                    count = 1
                )
            ), bagItems
        )

        SUT.addBagItem(ITEM_1, "l")
        assertEquals(2, bagSize)
        assertEquals(
            listOf(
                BagItem(
                    bagItemId = 1,
                    item = ITEM_1,
                    size = "l",
                    count = 2
                )
            ), bagItems
        )

        SUT.addBagItem(ITEM_1, "xl")
        assertEquals(3, bagSize)
        assertEquals(
            listOf(
                BagItem(
                    bagItemId = 1,
                    item = ITEM_1,
                    size = "l",
                    count = 2
                ),
                BagItem(
                    bagItemId = 2,
                    item = ITEM_1,
                    size = "xl",
                    count = 1
                ),
            ), bagItems
        )

        SUT.removeBagItem(1)
        assertEquals(2, bagSize)
        assertEquals(
            listOf(
                BagItem(
                    bagItemId = 1,
                    item = ITEM_1,
                    size = "l",
                    count = 1
                ),
                BagItem(
                    bagItemId = 2,
                    item = ITEM_1,
                    size = "xl",
                    count = 1
                ),
            ), bagItems
        )

        SUT.removeBagItem(1)
        assertEquals(1, bagSize)
        assertEquals(
            listOf(
                BagItem(
                    bagItemId = 2,
                    item = ITEM_1,
                    size = "xl",
                    count = 1
                ),
            ), bagItems
        )

        SUT.cleanBag()
        assertEquals(0, bagSize)
        assertEquals(listOf<BagItem>(), bagItems)
    }

    // region helper methods

    // endregion helper methods

}