package com.mzakialkhairi.githubsearch.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.adapter.UserItemAdapter
import com.mzakialkhairi.githubsearch.databinding.ActivityMainBinding
import com.mzakialkhairi.githubsearch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : UserItemAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        adapter = UserItemAdapter()
        adapter.notifyDataSetChanged()

        binding.rvListUsers.layoutManager = LinearLayoutManager(this)
        binding.rvListUsers.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.progressBar.visibility = View.INVISIBLE
        binding.notifInsert.visibility = View.VISIBLE

        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                binding.notifInsert.visibility = View.GONE
                showLoading(false)
            }
        })

        binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.notifInsert.visibility = View.GONE
                sendUsername(query)
                return true
            }
            override fun onQueryTextChange(query: String): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_setting -> {
                val mIntent = Intent(this,SettingActivity::class.java)
                startActivity(mIntent)
            }
            R.id.menu_favorite -> {
                val mIntent = Intent(this,FavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendUsername(username : String){
        if (username.isEmpty()){
            Toast.makeText(this,"Enter the username correctly", Toast.LENGTH_SHORT).show()
        }
        else{
            showLoading(true)
            mainViewModel.setUsername(username)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }


}
