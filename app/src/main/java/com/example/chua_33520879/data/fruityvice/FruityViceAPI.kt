package com.example.chua_33520879.data.fruityvice

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceAPI {
    @GET("fruit/all")
    suspend fun getAllFruits(): Response<List<FruitResponseModel>>

    @GET("fruit/{name}")
    suspend fun getFruit(
        @Path("name") name: String
    ): FruitResponseModel

    companion object {
        var BASE_URL = "https://www.fruityvice.com/api/"

        fun create(): FruityViceAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(FruityViceAPI::class.java)
        }
    }
}