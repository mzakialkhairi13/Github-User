package com.mzakialkhairi.githubsearch.view.activity

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.adapter.FavoriteAdapter
import com.mzakialkhairi.githubsearch.database.UserFavoriteHelper
import com.mzakialkhairi.githubsearch.databinding.ActivityFavoriteBinding
import com.mzakialkhairi.githubsearch.helper.MappingHelper
import com.mzakialkhairi.githubsearch.model.UserFavorite
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var ufHelper: UserFavoriteHelper
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_favorite)

        supportActionBar?.title = "User Favorite"

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rv_favorite.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ufHelper = UserFavoriteHelper.getInstance(applicationContext)
        ufHelper.open()

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {

                val cursor = ufHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val ufs = deferredNotes.await()
            if (ufs.size > 0) {
                adapter.listFavorite= ufs
            } else {
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onDestroy() {
        super.onDestroy()
        ufHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> backHome()
        }
        return super.onOptionsItemSelected(item)
    }

    fun backHome(){
        val mIntent = Intent(this,MainActivity::class.java)
        startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }
}

