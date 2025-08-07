package com.example.chua_33520879.data.fruityvice

class FruityViceRepository {
    val apiService = FruityViceAPI.create()

    suspend fun getSpecificFruit(fruitName: String): FruitResponseModel {
        return apiService.getFruit(fruitName)
    }

    suspend fun getListOfFruit(): List<String> {
        val response = apiService.getAllFruits().body()
        return response?.map { data -> data.name } ?: emptyList()
    }
}