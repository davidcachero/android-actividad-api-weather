package com.example.tareaapiweatherdavid.fragments

import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.adapters.CitiesAdapter
import com.example.tareaapiweatherdavid.adapters.ItemAdapter
import com.example.tareaapiweatherdavid.database.Cities
import com.example.tareaapiweatherdavid.database.DataRepository
import com.example.tareaapiweatherdavid.model.MyViewModel
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Favorites : Fragment(), ActionMode.Callback {
    private var selector = false
    private var actionMode:ActionMode? = null
    private var selectorId:MutableList<String> = ArrayList()
    private lateinit var mainUpdate:MenuItem
    private lateinit var mainCompare:MenuItem
    private lateinit var itemList:RecyclerView
    lateinit var cityAdapter:CitiesAdapter
    lateinit var cities:List<Cities>
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = bundleOf(Pair("city", ""))
                findNavController().navigate(
                    FavoritesDirections.actionFavoritesFragmentToMainWeather(bundle)
                )
            }
        })
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.favorites_fragment, container, false)
        itemList = v.findViewById(R.id.recyclerviewlist)
        onCitiesList()
        return v
    }

    override fun onCreateActionMode(mode:ActionMode, menu:Menu):Boolean {
        val inflater = mode.menuInflater
        inflater.inflate(R.menu.menu_modify_compare, menu)
        mainUpdate = menu.findItem(R.id.Modify)
        mainCompare = menu.findItem(R.id.Compare)
        return true
    }

    override fun onCreateOptionsMenu(menu:Menu, inflater:MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        val like = menu.findItem(R.id.like)
        val dislike = menu.findItem(R.id.dislike)
        val likes = menu.findItem(R.id.Favorites)
        val mainWeather = menu.findItem(R.id.OpenWeatherMap)
        like.isVisible = false
        dislike.isVisible = false
        likes.isVisible = false
        mainWeather.isVisible = true
        val menuItemSearch = menu.findItem(R.id.app_bar_search)
        menuItemSearch.isVisible = true
        val searchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = "Buscar"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String?):Boolean {
                return false
            }

            override fun onQueryTextChange(newText:String?):Boolean {
                cityAdapter.filter.filter(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareActionMode(mode:ActionMode, menu:Menu):Boolean {
        return false
    }

    override fun onDestroyActionMode(mode:ActionMode?) {
        mainCompare.isVisible = false
        mainUpdate.isVisible = false
        actionMode = null
        selector = false
        selectorId = ArrayList()
        cityAdapter.setId(ArrayList())
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onActionItemClicked(mode:ActionMode, menuItem:MenuItem):Boolean {
        when (menuItem.itemId) {
            R.id.Compare -> {
                val cities = mutableListOf<Cities>()
                if (selectorId.size == 2) {
                    for (data in this.cities) {
                        if (selectorId.contains(data.name)) {
                            cities.add(Cities(data.name))
                        }
                    }
                    findNavController().navigate(
                        FavoritesDirections.actionFavoritesFragmentToCompareFragment(
                            cities[0].name,
                            cities[1].name
                        )
                    )
                    mode.finish()
                    return true
                } else {
                    Toast.makeText(
                        context,
                        "Selecciona 2 ciudades para comparar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            R.id.Modify -> {
                val selector = mutableListOf<Cities>()
                if (selectorId.size == 1) {
                    for (data in cities) {
                        if (selectorId.contains(data.name)) {
                            selector.add(Cities(data.name))
                        }
                    }
                    findNavController().navigate(
                        FavoritesDirections.actionFavoritesFragmentToModifyFragment(
                            selector[0].name
                        )
                    )
                    mode.finish()
                    return true
                } else {
                    Toast.makeText(
                        context,
                        "Selecciona sólo 1 ciudad para modificar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }
        }
        return false
    }

    private fun onCitiesList() {
        val preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = preferences?.getString("user", "")
        val dataRepository = DataRepository(requireContext())
        cities = dataRepository.getCities(user.toString())[0].cities
        val viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        cityAdapter = CitiesAdapter(context, cities, viewModel)
        itemList.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        itemList.adapter = cityAdapter
        itemList.addOnItemTouchListener(
            ItemAdapter(context, itemList, object : ItemAdapter.OnItemClickListener {
                override fun onClick(view:View?, position:Int) {
                    if (selector) {
                        onTouchMulti(position)
                    }
                }

                override fun onTouchClick(view:View?, position:Int) {
                    if (!selector) {
                        selectorId = ArrayList()
                        selector = true
                        if (actionMode == null) {
                            actionMode = activity!!.startActionMode(this@Favorites)
                        }
                    }
                    (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                    onTouchMulti(position)
                }
            })
        )
    }

    private fun onTouchMulti(position:Int) {
        val data = cityAdapter.getId(position)
        if (actionMode != null) {
            if (selectorId.contains(data.name)) selectorId.remove(data.name)
            else selectorId.add(data.name)
            if (selectorId.size > 0) actionMode!!.title = selectorId.size.toString()
            else {
                actionMode!!.title = ""
                actionMode!!.finish()
            }
            cityAdapter.setId(selectorId)
        }
    }
}