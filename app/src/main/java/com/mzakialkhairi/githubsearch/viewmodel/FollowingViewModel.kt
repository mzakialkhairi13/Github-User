package com.mzakialkhairi.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mzakialkhairi.githubsearch.Constant
import com.mzakialkhairi.githubsearch.model.UserFollowing
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {

    private val listFollowings = MutableLiveData<java.util.ArrayList<UserFollowing>>()

    fun setUsername(username: String) {
        val listFollowing = ArrayList<UserFollowing>()

        val token = Constant.TOKEN
        val url = Constant.URL_DETAIL+username+"/following"

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
                        val userFollowing = UserFollowing()
                        userFollowing.id = user.getInt("id")
                        userFollowing.username = user.getString("login")
                        userFollowing.avatar = user.getString("avatar_url")
                        userFollowing.url = user.getString("html_url")
                        listFollowing.add(userFollowing)
                    }

                    listFollowings.postValue(listFollowing)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }
    fun getUsers(): MutableLiveData<java.util.ArrayList<UserFollowing>> {

        return listFollowings
    }

}