package com.example.onlishop.base

abstract class Model {
    fun contentIdentical(other: Model): Boolean = this == other
}