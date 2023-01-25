package com.example.onlishop.ui.shop.search

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.example.onlishop.R
import com.example.onlishop.base.BaseFragment
import com.example.onlishop.global.hideKeyboard
import com.example.onlishop.models.IconAction
import com.example.onlishop.models.Item
import com.example.onlishop.ui.composables.ItemsColumn
import com.example.onlishop.ui.composables.general.LineTextSpacer
import com.example.onlishop.ui.composables.general.ShopFloating
import com.example.onlishop.ui.composables.general.ToolBar
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentSearch: BaseFragment(R.layout.fragment_compose) {

    private val viewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view as ComposeView).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val itemsState = viewModel.items.observeAsState()
                val bagCountState = viewModel.bagCount.observeAsState(0)
                val searchState = viewModel.currentSearch.collectAsState()


                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        ToolBar(
                            title = stringResource(id = R.string.text_search),
                            navigationAction = IconAction(R.drawable.ic_back, this@FragmentSearch::onBackPressed),
                        )

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = searchState.value,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            keyboardActions = KeyboardActions {
                                hideKeyboard()
                            },
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                backgroundColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            onValueChange = viewModel::setSearch
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
                            onItemClicked = this@FragmentSearch::onItemClick
                        )
                    }

                    ShopFloating(
                        bagSize = bagCountState.value,
                        modifier = Modifier
                            .padding(end = 20.dp, bottom = 20.dp)
                            .size(75.dp)
                            .align(Alignment.BottomEnd),
                    ) {
                        val action = FragmentSearchDirections.toBag()
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }

    private fun onItemClick(item: Item) {
//        binding.editSearch.hideKeyboard()
        logger.logExecution("onItemClick")
        val action = FragmentSearchDirections.toDetail()
        action.itemId = item.id
        findNavController().navigate(action)
    }

}