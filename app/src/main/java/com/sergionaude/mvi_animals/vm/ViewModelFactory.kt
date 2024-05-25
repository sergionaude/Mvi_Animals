package com.sergionaude.mvi_animals.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergionaude.mvi_animals.api.AnimalApi
import com.sergionaude.mvi_animals.api.AnimalRepo
import java.lang.IllegalArgumentException

class ViewModelFactory(private val animalApi: AnimalApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimalViewModel::class.java)){
            return AnimalViewModel(AnimalRepo(animalApi)) as T
        }
        throw IllegalArgumentException("Unknown class or parameter")
    }
}