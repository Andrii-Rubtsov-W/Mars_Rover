package com.portugal1576.marsrover.data.api

import com.portugal1576.marsrover.data.model.PlayElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("")
    suspend fun getWeather(
        @Query("name") name: String
    ): Response<PlayElement>
}