package com.example.chua_33520879.data.fruityvice

data class FruitResponseModel(
    var name: String,
    var family: String,
    var order: String,
    var genus: String,
    var nutritions: Nutritions
)

data class Nutritions(
    var calories: Double,
    var fat: Double,
    var sugar: Double,
    var carbohydrates: Double,
    var protein: Double
)