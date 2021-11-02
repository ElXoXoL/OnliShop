package com.example.onlishop.utils

import java.util.*

object IdGenerator {

    val randomId: String get() = UUID.randomUUID().toString().substring(0..7)

}