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
import com.mzakialkhairi.githubsearch.adapter.FollowingAdapter
import com.mzakialkhairi.githubsearch.databinding.FragmentFollowingBinding
import com.mzakialkhairi.githubsearch.viewmodel.FollowingViewModel

class FollowingFragment(username : String) : Fragment() {

    private lateinit var binding : FragmentFollowingBinding
    private var uname = username
    private lateinit var adapter : FollowingAdapter
    private lateinit var vModel : FollowingViewModel

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_following, container, false)

        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollowing.layoutManager = LinearLayoutManager(this.context)
        binding.rvFollowing.adapter = adapter

        vModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)

        vModel.setUsername(uname)

        vModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
            }
        })


        return binding.root
    }


}
