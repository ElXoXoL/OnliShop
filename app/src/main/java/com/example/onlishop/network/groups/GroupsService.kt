package com.example.onlishop.network.groups

import com.example.onlishop.database.models.ShopGroup
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface GroupsService {

    fun getGroups(): List<ShopGroup>

    fun getGroupsRx(): Single<List<ShopGroup>>

}