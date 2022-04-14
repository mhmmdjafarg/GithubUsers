package com.pbd.githubusers

import android.content.Context
import androidx.appcompat.widget.SearchView
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pbd.githubusers.adapter.ListUserAdapter
import com.pbd.githubusers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var rvGithubUser: RecyclerView
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GithubUsers)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerList()

        // Preferences dark/light theme
        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        mainViewModel.responseUserSearch.observe(this) { responseUserData ->
            showData(responseUserData)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.cari_username_github)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getUserFromQueryApi(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorites -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            R.id.switch_dark -> {
                mainViewModel.saveThemeSetting(true)
                true
            }
            R.id.switch_light -> {
                mainViewModel.saveThemeSetting(false)
                true
            }
            else -> true
        }
    }

    private fun showRecyclerList() {
        rvGithubUser = binding.rvGithubusers
        listUserAdapter = ListUserAdapter(arrayListOf())
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvGithubUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvGithubUser.layoutManager = LinearLayoutManager(this)
        }
        rvGithubUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemSearch) {
                val moveToDetailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveToDetailIntent.putExtra(DetailUserActivity.EXTRA_USER, data.login)
                startActivity(moveToDetailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showData(responseData: ResponseUserSearch) {
        if (responseData.totalCount == 0) {
            binding.noResult.visibility = View.VISIBLE
            rvGithubUser.visibility = View.GONE
        } else {
            binding.noResult.visibility = View.GONE
            rvGithubUser.visibility = View.VISIBLE
            listUserAdapter.setData(responseData.items as List<ItemSearch>)
        }
    }

}

