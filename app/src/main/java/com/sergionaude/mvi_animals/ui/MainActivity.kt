package com.sergionaude.mvi_animals.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sergionaude.mvi_animals.api.AnimalService
import com.sergionaude.mvi_animals.databinding.ActivityMainBinding
import com.sergionaude.mvi_animals.model.AnimalItem
import com.sergionaude.mvi_animals.view.AnimalListAdapter
import com.sergionaude.mvi_animals.view.MainIntent
import com.sergionaude.mvi_animals.view.MainState
import com.sergionaude.mvi_animals.vm.AnimalViewModel
import com.sergionaude.mvi_animals.vm.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    val binding : ActivityMainBinding
        get() = _binding!!

    private lateinit var viewModel: AnimalViewModel
    private val adapter = AnimalListAdapter(mutableListOf<AnimalItem>())
    private lateinit var recyclerView : RecyclerView
    private lateinit var btnFetchAnimals : Button
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        btnFetchAnimals = binding.buttonFetchAnimals
        progressBar = binding.progressBar
        recyclerView = binding.recyclerView
        setUpUI()
        observers()
        setContentView(binding.root)
    }


    private fun setUpUI(){
        viewModel = ViewModelProvider(this, ViewModelFactory(AnimalService.api))[AnimalViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        recyclerView.adapter = adapter
        btnFetchAnimals.setOnClickListener {
            lifecycleScope.launch {
                viewModel.userIntent.send(MainIntent.FetchAnimals)
                println("Pressed fetchAnimals")
            }
        }
    }

    private fun observers(){
        lifecycleScope.launch {
            viewModel.state.collect(){collector ->
                println("Changes from observer $collector")
                when(collector){
                    is MainState.Idle -> {}
                    is MainState.Loading -> {
                        btnFetchAnimals.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                    is MainState.Animals -> {
                        btnFetchAnimals.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        collector.animals.let {
                            adapter.updateListAnimals(it)
                        }
                    }
                    is MainState.Error -> {
                        btnFetchAnimals.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                        val text = collector.error
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(this@MainActivity, text, duration)
                        toast.show()
                    }
                }
            }
        }
    }

}