package com.example.bjjcompanion.data.remote.api

import com.example.bjjcompanion.data.remote.dto.FoodSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsdaFoodApi {
    @GET("foods/search")
    suspend fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("pageSize") pageSize: Int = 25
    ): FoodSearchResponse
}
