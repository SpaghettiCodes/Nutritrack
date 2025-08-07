package com.example.chua_33520879.data.ai

data class FoodAnalysisResult(
    val name: String,
    val calories: Float,
    val ingredients: List<String>,
    val nutrition: List<String>,
    val recommend: Int,
    val reasons: List<String>
)