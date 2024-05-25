package com.sergionaude.mvi_animals.api

import com.sergionaude.mvi_animals.model.Animal
import com.sergionaude.mvi_animals.model.AnimalItem
import retrofit2.http.GET

interface AnimalApi {

    @GET("animals.json")
    suspend fun getListAnimals() : Animal
}