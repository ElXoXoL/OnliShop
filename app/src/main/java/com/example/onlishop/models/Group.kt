package com.example.onlishop.models

import com.example.onlishop.base.Model

class Group (
        val id: Int = 0,
        val parentGroupId: Int? = null,
        val name: String = "",
): Model()