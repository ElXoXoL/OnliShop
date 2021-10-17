package com.example.onlishop.models

import com.example.onlishop.base.Model

class Item(
        val id: Int = 0,
        val groupId: Int = 0,
        val name: String = "",
        val description: String = "",
        val imageDrawable: Int = 0,
        val price: Int = 0,
        val sizes: List<String> = emptyList(),
    ): Model()