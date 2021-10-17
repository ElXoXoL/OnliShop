package com.example.onlishop.repository

import com.example.onlishop.models.Group
import com.example.onlishop.models.Item

interface ItemRepository {

    suspend fun getGroups(): List<Group>

    suspend fun getItems(): List<Item>

}