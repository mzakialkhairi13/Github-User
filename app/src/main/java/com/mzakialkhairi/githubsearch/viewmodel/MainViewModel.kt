package com.mzakialkhairi.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mzakialkhairi.githubsearch.Constant
import com.mzakialkhairi.githubsearch.model.UserItems
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.ArrayList

class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUsername(username: String) {
        val listItems = ArrayList<UserItems>()
        val token = Constant.TOKEN
        val url = Constant.URL_LIST+username

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", token)
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")
                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItems = UserItems()
                        userItems.id = user.getInt("id")
                        userItems.username = user.getString("login")
                        userItems.avatar = user.getString("avatar_url")
                        userItems.url = user.getString("html_url")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<UserItems>> {

        return listUsers
    }

}
