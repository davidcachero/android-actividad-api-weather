package com.example.tareaapiweatherdavid.adapters

import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.database.Cities
import com.example.tareaapiweatherdavid.fragments.FavoritesDirections
import com.example.tareaapiweatherdavid.model.MyViewModel
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList

class CitiesAdapter(
    val context:Context?, var cities:List<Cities>, private var myViewModel:MyViewModel
) : RecyclerView.Adapter<CitiesAdapter.MyViewHolder>(), Filterable {
    var list = ArrayList<Cities>()
    private var id:List<String> = ArrayList()

    inner class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        var textView:TextView = itemView.findViewById(R.id.textViewTitle)
        fun bindItems(cities:Cities) {
            val textViewName = itemView.findViewById<TextView>(R.id.textViewTitle)
            textViewName.text = cities.name
        }
    }

    override fun getItemCount():Int {
        return list.size
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):CitiesAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cities_list, parent, false)
        return MyViewHolder(v)
    }

    override fun getFilter():Filter {
        return object : Filter() {
            override fun performFiltering(constraint:CharSequence?):FilterResults {
                val char = constraint.toString()
                list = if (char.isEmpty()) {
                    cities as ArrayList<Cities>
                } else {
                    val rs = ArrayList<Cities>()
                    for (row in cities) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(char.toLowerCase(Locale.ROOT))
                        ) {
                            rs.add(row)
                        }
                    }
                    rs
                }
                val newResult = FilterResults()
                newResult.values = list
                return newResult
            }

            override fun publishResults(constraint:CharSequence?, results:FilterResults?) {
                list = results?.values as ArrayList<Cities>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder:MyViewHolder, position:Int) {
        val bdl = bundleOf(Pair("cities", list[position].name))
        val fav = FavoritesDirections.actionFavoritesFragmentToMainWeather(bdl)
        holder.textView.text = cities[position].name
        holder.bindItems(list[position])
        holder.itemView.setOnClickListener {
            myViewModel.setMeCity(list[position])
            it.findNavController().navigate(fav)
        }
    }

    init {
        list = cities as ArrayList<Cities>
    }

    fun getId(position:Int):Cities {
        return cities[position]
    }

    fun setId(selectedIds:List<String>) {
        this.id = selectedIds
        notifyDataSetChanged()
    }
}