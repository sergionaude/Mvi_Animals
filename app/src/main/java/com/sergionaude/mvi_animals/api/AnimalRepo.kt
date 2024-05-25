package com.sergionaude.mvi_animals.api

class AnimalRepo(private val animalApi: AnimalApi) {

    suspend fun getAnimalList() = animalApi.getListAnimals()

}