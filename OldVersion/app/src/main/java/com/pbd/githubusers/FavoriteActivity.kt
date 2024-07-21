package com.pbd.githubusers

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pbd.githubusers.adapter.ListFavoritesAdapter
import com.pbd.githubusers.databinding.ActivityFavoriteBinding
import com.pbd.githubusers.room.Favorite
import com.pbd.githubusers.room.FavoriteDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var rvFavorites: RecyclerView
    private lateinit var listFavoritesAdapter: ListFavoritesAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private val db by lazy { FavoriteDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerList()
        getFavoritesFromDB()
    }

    override fun onResume() {
        super.onResume()
        getFavoritesFromDB()
    }

    private fun showRecyclerList() {
        rvFavorites = binding.rvFavorites
        listFavoritesAdapter = ListFavoritesAdapter(arrayListOf())
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvFavorites.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvFavorites.layoutManager = LinearLayoutManager(this)
        }
        rvFavorites.adapter = listFavoritesAdapter

        listFavoritesAdapter.setOnItemClickCallback(object : ListFavoritesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                val moveToDetailIntent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                moveToDetailIntent.putExtra(DetailUserActivity.EXTRA_USER, data.username)
                startActivity(moveToDetailIntent)
            }
        })
    }

    private fun getFavoritesFromDB() = CoroutineScope(Dispatchers.IO).launch {
        val favoriteList = db.favoriteDao().getAllFavorites()
        CoroutineScope(Dispatchers.Main).launch {
            showLoading(true)
            showData(favoriteList)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showData(responseData: List<Favorite>) {
        if (responseData.isEmpty()) {
            binding.noResult.visibility = View.VISIBLE
            rvFavorites.visibility = View.GONE
        } else {
            binding.noResult.visibility = View.GONE
            rvFavorites.visibility = View.VISIBLE
            listFavoritesAdapter.setData(responseData)
        }
    }
}