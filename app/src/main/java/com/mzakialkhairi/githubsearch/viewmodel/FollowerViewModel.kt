package com.mzakialkhairi.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mzakialkhairi.githubsearch.Constant
import com.mzakialkhairi.githubsearch.model.UserFollower
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerViewModel : ViewModel() {

    private val listFollowers = MutableLiveData<java.util.ArrayList<UserFollower>>()

    fun setUsername(username: String) {
        val listFollower = ArrayList<UserFollower>()

        val token = Constant.TOKEN
        val url = Constant.URL_DETAIL+username+"/followers"

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", token)
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()) {
                        val user = responseObject.getJSONObject(i)
                        val userFollower = UserFollower()
                        userFollower.id = user.getInt("id")
                        userFollower.username = user.getString("login")
                        userFollower.avatar = user.getString("avatar_url")
                        userFollower.url = user.getString("html_url")
                        listFollower.add(userFollower)
                    }

                    listFollowers.postValue(listFollower)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }
    fun getUsers(): MutableLiveData<java.util.ArrayList<UserFollower>> {

        return listFollowers
    }

}