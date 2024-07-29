package com.example.githubusers.ui.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.GithubApp
import com.example.githubusers.R
import com.example.githubusers.data.Result
import com.example.githubusers.data.remote.response.User
import com.example.githubusers.databinding.FragmentFollowingBinding
import com.example.githubusers.ui.ViewModelFactory
import com.example.githubusers.ui.main.ListUserAdapter
import javax.inject.Inject

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding

    private lateinit var rvFollowingFollowers : RecyclerView
    private lateinit var listUserAdapter: ListUserAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as GithubApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]

        rvFollowingFollowers = requireView().findViewById(R.id.foll_list)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        when(index) {
            1 -> username?.let {
                viewModel.getFollowings(username).observe(requireActivity()) { result ->
                    result?.let {
                        when (it) {
                            is Result.Loading -> {
                                binding?.progressBarFragment?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBarFragment?.visibility = View.GONE
                                val listUsers = it.data
                                if (listUsers != null) {
                                    listUserAdapter.updateData(listUsers)
                                }
                            }
                            is Result.Error -> {
                                binding?.progressBarFragment?.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            else -> username?.let {
                viewModel.getFollowers(username).observe(requireActivity()) { result ->
                    result?.let {
                        when (it) {
                            is Result.Loading -> {
                                binding?.progressBarFragment?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBarFragment?.visibility = View.GONE
                                val listUsers = it.data
                                if (listUsers != null) {
                                    listUserAdapter.updateData(listUsers)
                                }
                            }
                            is Result.Error -> {
                                binding?.progressBarFragment?.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
        showRecyclerList()
    }

    private fun showRecyclerList() {
        listUserAdapter = ListUserAdapter(arrayListOf())
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvFollowingFollowers.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            rvFollowingFollowers.layoutManager = LinearLayoutManager(requireContext())
        }
        rvFollowingFollowers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val moveToDetailIntent = Intent(context, DetailActivity::class.java)
                moveToDetailIntent.putExtra(DetailActivity.EXTRA_USER, data.login)
                startActivity(moveToDetailIntent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}