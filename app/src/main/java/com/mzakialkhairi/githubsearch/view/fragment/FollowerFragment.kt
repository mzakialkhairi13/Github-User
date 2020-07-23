package com.mzakialkhairi.githubsearch.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.adapter.FollowerAdapter
import com.mzakialkhairi.githubsearch.databinding.FragmentFollowerBinding
import com.mzakialkhairi.githubsearch.viewmodel.FollowerViewModel

class FollowerFragment(username: String) : Fragment() {

    private lateinit var binding : FragmentFollowerBinding
    private var uname = username
    private lateinit var adapter : FollowerAdapter
    private lateinit var vModel : FollowerViewModel

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_follower, container, false)

        adapter = FollowerAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollower.layoutManager = LinearLayoutManager(this.context)
        binding.rvFollower.adapter = adapter

        vModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerViewModel::class.java)

        vModel.setUsername(uname)

        vModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
            }
        })


        return binding.root
    }

}