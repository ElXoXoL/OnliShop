package com.example.onlishop.network.groups

import com.example.onlishop.database.models.ShopGroup

interface GroupsService {

    fun getGroups(): List<ShopGroup>

}