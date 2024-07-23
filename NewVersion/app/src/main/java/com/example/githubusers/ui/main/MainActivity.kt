package com.example.githubusers.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.GithubApp
import com.example.githubusers.R
import com.example.githubusers.data.Result
import com.example.githubusers.data.remote.User
import com.example.githubusers.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvGithubUser: RecyclerView
    private lateinit var listUserAdapter: ListUserAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Dagger
        (application as GithubApp).appComponent.inject(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        // Observe the search query and perform search
        viewModel.searchQuery.observe(this) { query ->
            query?.let {
                viewModel.searchUsers(it).observe(this) { result ->
                    result?.let {
                        when (it) {
                            is Result.Loading -> Log.d("SEARCH_LOADING", "Result loading")
                            is Result.Success -> {
                                val listUsers = it.data
                                listUserAdapter.updateData(listUsers)
                            }

                            is Result.Error -> Log.e("SEARCH_ERROR", it.error)
                        }
                    }
                }
            }
        }

        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.find_github_username_here)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setSearchQuery(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
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
            override fun onItemClicked(data: User) {
                Toast.makeText(this@MainActivity, "Clicked user " + data.login, Toast.LENGTH_SHORT).show()
            }
        })
    }
}