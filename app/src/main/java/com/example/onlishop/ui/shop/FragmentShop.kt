package com.example.onlishop.ui.shop

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.global.viewBinding
import com.example.onlishop.models.Group
import com.example.onlishop.models.Item
import com.example.onlishop.models.IconAction
import com.example.onlishop.ui.composables.GroupsRow
import com.example.onlishop.ui.composables.ItemsColumn
import com.example.onlishop.ui.composables.general.LineTextSpacer
import com.example.onlishop.ui.composables.general.ShopFloating
import com.example.onlishop.ui.composables.general.ToolBar
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentShop: BaseFragment(R.layout.fragment_compose) {

    private val viewModel: ShopViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view as ComposeView).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val selectedGroupState = viewModel.selectedGroup.collectAsState()
                val groupsState = viewModel.groups.observeAsState()
                val itemsState = viewModel.items.observeAsState()
                val bagCountState = viewModel.bagCount.observeAsState(0)

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        ToolBar(
                            title = selectedGroupState.value?.name.orEmpty(),
                            navigationAction = IconAction(R.drawable.ic_search, this@FragmentShop::onSearchClick),
                            actions = listOf(
                                IconAction(R.drawable.ic_account, this@FragmentShop::onUserClick)
                            )
                        )
                        LineTextSpacer(
                            text = getString(R.string.text_groups_title),
                            modifier = Modifier
                                .requiredHeight(25.dp)
                                .fillMaxWidth()
                        )

                        GroupsRow(
                            listItems = groupsState.value.orEmpty(),
                            modifier = Modifier.height(100.dp).fillMaxWidth(),
                            onItemClicked = this@FragmentShop::onGroupClick
                        )

                        LineTextSpacer(
                            text = getString(R.string.text_items_title),
                            modifier = Modifier
                                .requiredHeight(25.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        ItemsColumn(
                            listItems = itemsState.value.orEmpty(),
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxSize(),
                            onItemClicked = this@FragmentShop::onItemClick
                        )
                    }

                    ShopFloating(
                        bagSize = bagCountState.value,
                        modifier = Modifier
                            .padding(end = 20.dp, bottom = 20.dp)
                            .size(75.dp)
                            .align(Alignment.BottomEnd),
                        onShopClick = this@FragmentShop::onBagClick
                    )
                }

            }
        }

    }

    private fun onGroupClick(item: Group) = viewModel.selectGroup(item.id)

    private fun onItemClick(item: Item) {
        logger.logExecution("onItemClick")
        val action = FragmentShopDirections.toDetail()
        action.itemId = item.id
        findNavController().navigate(action)
    }

    private fun onSearchClick(v: View? = null) {
        logger.logExecution("onSearchClick")
        val action = FragmentShopDirections.toSearch()
        findNavController().navigate(action)
    }

    private fun onUserClick(v: View? = null) {
        logger.logExecution("onUserClick")
        val action = FragmentShopDirections.toUser()
        findNavController().navigate(action)
    }

    private fun onBagClick(v: View? = null) {
        logger.logExecution("onBagClick")
        val action = FragmentShopDirections.toBag()
        findNavController().navigate(action)
    }

    override fun onBackPressed() {
        val selectedGroup = viewModel.selectedGroup.value
        if (selectedGroup?.parentGroupId != null) {
            viewModel.selectGroup(selectedGroup.parentGroupId)
            return
        }
        super.onBackPressed()
    }
}