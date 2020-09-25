package com.mzakialkhairi.githubsearch.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.adapter.FavoriteHorizontalAdapter
import com.mzakialkhairi.githubsearch.adapter.UserItemAdapter
import com.mzakialkhairi.githubsearch.database.DatabaseContract
import com.mzakialkhairi.githubsearch.databinding.ActivityMainBinding
import com.mzakialkhairi.githubsearch.helper.MappingHelper
import com.mzakialkhairi.githubsearch.model.UserFavorite
import com.mzakialkhairi.githubsearch.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : UserItemAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var ufAdapter: FavoriteHorizontalAdapter

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        // show user favorite
        binding.rvListUsersHorizontal.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvListUsersHorizontal.setHasFixedSize(true)
        ufAdapter = FavoriteHorizontalAdapter(this)
        binding.rvListUsersHorizontal.adapter = ufAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUsersFavoriteAsync()
            }
        }
        contentResolver.registerContentObserver(DatabaseContract.UserFavoriteColumns.CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadUsersFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                ufAdapter.listFavorite = list
            }
        }

        //show search user list
        adapter = UserItemAdapter(this)
        adapter.notifyDataSetChanged()

        binding.rvListUsers.layoutManager = LinearLayoutManager(this)
        binding.rvListUsers.adapter = adapter
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.progressBar.visibility = View.INVISIBLE
        binding.notifInsertUsername.visibility = View.VISIBLE

        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                binding.notifInsertUsername.visibility = View.GONE
                showLoading(false)
            }
        })

        binding.selengkapnyaFavorite.setOnClickListener {
        val intent = Intent(this,FavoriteActivity::class.java)
            startActivity(intent)
        }

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
                binding.notifInsertUsername.visibility = View.GONE
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendUsername(username : String){
        if (username.isEmpty()){
            showSnackbar("Input username corectly")
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

    private fun showSnackbar(message : String){
        Snackbar.make(binding.rvListUsers,message, Snackbar.LENGTH_SHORT).show()
    }

    private fun loadUsersFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(DatabaseContract.UserFavoriteColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val ufs = deferredNotes.await()
            if (ufs.size > 0) {
                ufAdapter.listFavorite= ufs
                binding.tvNoFavorite.visibility = View.GONE
            } else {
                ufAdapter.listFavorite = ArrayList()
                binding.rvListUsersHorizontal.visibility = View.GONE
            }
        }
    }

}
