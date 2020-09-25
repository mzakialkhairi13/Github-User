package com.mzakialkhairi.githubsearch.view.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.adapter.SectionPagerAdapter
import com.mzakialkhairi.githubsearch.database.DatabaseContract
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.mzakialkhairi.githubsearch.database.UserFavoriteHelper
import com.mzakialkhairi.githubsearch.databinding.ActivityDetailBinding
import com.mzakialkhairi.githubsearch.helper.MappingHelper
import com.mzakialkhairi.githubsearch.model.UserDetail
import com.mzakialkhairi.githubsearch.model.UserFavorite
import com.mzakialkhairi.githubsearch.provider.GithubProvider
import com.mzakialkhairi.githubsearch.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private var uFavorite: UserFavorite? = null
    private var avatar: String? = null
    private lateinit var uriWithUsername: Uri
    private lateinit var provider : GithubProvider
    private lateinit var ufHelper: UserFavoriteHelper
    private var position: Int = 0

    var statusFavorite : Boolean? = false

    companion object{
        var username = "username"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail)

        ufHelper = UserFavoriteHelper.getInstance(applicationContext)
        ufHelper.open()
        val username = intent.getStringExtra(username)

        uFavorite = intent.getParcelableExtra(EXTRA_NOTE)
        if (uFavorite != null){
            position = intent.getIntExtra(EXTRA_POSITION,0)
        }

        //cek favorite
        cekFavorite(username)

        if (statusFavorite == true) {
            uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + uFavorite?.username)
            val cursor = contentResolver.query(uriWithUsername, null, null, null, null)
            if (cursor != null) {
                uFavorite = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }

        }

        showLoading(true)
        val model = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)

        model.setUsers(username)

        model.getUser()?.observe(this, Observer {
                user -> if (user != null){
            bind(user)
            showLoading(false)
        }
        })


        binding.viewPager.adapter = SectionPagerAdapter(supportFragmentManager,username)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.favoriteButton.setOnClickListener {
            statusFavorite = if (statusFavorite == true){
                deleteUserFavorite(username)
                !statusFavorite!!
            }else{
                setUserFavorite(username)
                !statusFavorite!!
            }
            cekFavorite(username)
        }
    }


    fun bind(user : List<UserDetail>){
        Picasso.with(applicationContext).load(user[0].avatar).into(binding.detailAvatar)
        binding.detailName.text = user[0].name
        binding.detailUsername.text = user[0].username
        binding.detailUrl.text = user[0].url
        binding.detailLocation.text = user[0].location

        avatar = user[0].avatar

        supportActionBar?.title  = user[0].username
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.detailProgressbar.visibility = View.VISIBLE
        }else{
            binding.detailProgressbar.visibility = View.GONE
        }
    }


    private fun setUserFavorite(username : String){
        uFavorite?.username = username

        val values = ContentValues()
        values.put(DatabaseContract.UserFavoriteColumns.USERNAME, username)
        values.put(DatabaseContract.UserFavoriteColumns.AVATAR, avatar)

        contentResolver.insert(CONTENT_URI, values)
        showSnackbar("$username ditambahkan ke daftar favorite")
    }

    private fun deleteUserFavorite(username : String){
        provider = GithubProvider()
        provider.deleteUser(username)
        showSnackbar("$username dihapus dari daftar favorite")
        
    }

    private fun cekFavorite(username : String){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = ufHelper.queryByUsername(username)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val uf = deferredNotes.await()
            statusFavorite = if (uf.size > 0) {
                binding.favoriteButton.setImageResource(R.drawable.faf)
                true
            } else {
                binding.favoriteButton.setImageResource(R.drawable.faf_border)
                false
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_setting_detail -> setting()
            android.R.id.home -> backHome()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setting(){
        val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(mIntent)
    }
    private fun backHome(){
        val mIntent = Intent(this,MainActivity::class.java)
        startActivity(mIntent)
    }

    private fun showSnackbar(message : String){
        Snackbar.make(binding.myCl,message, Snackbar.LENGTH_SHORT).show()
    }

}
