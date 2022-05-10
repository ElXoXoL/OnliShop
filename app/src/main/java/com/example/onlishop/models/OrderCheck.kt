package com.example.onlishop.models

data class OrderCheck(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val orderType: String = "",
    val delivery: String = "",
    val cardNum: String = "",
    val cardDate: String = "",
    val cardCvc: String = "",
    val isPrivacyAccepted: Boolean = false
)