package com.example.domain.model

data class BaggageDeclaration(
    val cabinBags: Int,
    val checkedBags: Int,
    val heavyBag: Boolean,
    val specialItem: String?
)
