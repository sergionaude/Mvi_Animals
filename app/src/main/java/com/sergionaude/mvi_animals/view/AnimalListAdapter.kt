package com.sergionaude.mvi_animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sergionaude.mvi_animals.R
import com.sergionaude.mvi_animals.api.AnimalService
import com.sergionaude.mvi_animals.model.AnimalItem

class AnimalListAdapter(private val animalList : MutableList<AnimalItem>) : RecyclerView.Adapter<AnimalListAdapter.ViewHolderAnimal>(){

    fun updateListAnimals(newListAnimals : List<AnimalItem>){
        animalList.clear()
        animalList.addAll(newListAnimals)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderAnimal(
        LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)
    )

    override fun getItemCount() = animalList.size

    override fun onBindViewHolder(holder: ViewHolderAnimal, position: Int) {
        holder.bind(animalItem = animalList[position])
    }

    inner class ViewHolderAnimal(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(animalItem: AnimalItem){
            itemView.findViewById<TextView>(R.id.nameAnimal).text = animalItem.name
            itemView.findViewById<TextView>(R.id.locationAnimal).text = animalItem.location
            itemView.findViewById<TextView>(R.id.speedAnimal).text = animalItem.speed.toString()
            val url = AnimalService.BASE_URL + animalItem.image
            Glide.with(itemView.findViewById<TextView>(R.id.nameAnimal).context)
                .load(url)
                .into(itemView.findViewById<ImageView>(R.id.imageAnimal))
        }
    }
}