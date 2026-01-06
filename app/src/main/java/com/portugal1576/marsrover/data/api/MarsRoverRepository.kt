package com.portugal1576.marsrover.data.api

import com.portugal1576.marsrover.data.model.PlayElement
import retrofit2.Response

class MarsRoverRepository(private val apiService: ApiService) {
    suspend fun getElement(name: String): Response<PlayElement> {
        return apiService.getWeather(name = name)
    }
}