package com.sergionaude.mvi_animals.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergionaude.mvi_animals.api.AnimalRepo
import com.sergionaude.mvi_animals.view.MainIntent
import com.sergionaude.mvi_animals.view.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AnimalViewModel(private val animalRepo: AnimalRepo) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private var _state = MutableStateFlow<MainState>(MainState.Idle)
    val state : StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent(){
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect(){ collector ->
                when(collector){
                    is MainIntent.FetchAnimals -> fetchAnimals()
                }
            }
        }
    }

    private fun fetchAnimals(){
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value =
                try {
                    println("Calling repository")
                    MainState.Animals(animalRepo.getAnimalList())
                }catch (exception:Exception){
                    MainState.Error(exception.localizedMessage)
                }
        }
    }
}