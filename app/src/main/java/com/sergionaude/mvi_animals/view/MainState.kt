package com.sergionaude.mvi_animals.view

import com.sergionaude.mvi_animals.model.AnimalItem

sealed class MainState{

    object Idle : MainState()
    object Loading : MainState()
    data class Animals(val animals: List<AnimalItem>) : MainState()
    data class Error(val error : String) : MainState()
}
