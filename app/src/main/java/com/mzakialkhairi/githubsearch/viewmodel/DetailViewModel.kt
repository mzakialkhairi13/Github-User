package com.mzakialkhairi.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mzakialkhairi.githubsearch.Constant
import com.mzakialkhairi.githubsearch.model.UserDetail
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    private var users: MutableLiveData<List<UserDetail>>? = null

    fun setUsers(username: String): LiveData<List<UserDetail>> {
        if (users == null) {
            users = MutableLiveData<List<UserDetail>>()
            loadUsers(username)
        }
        return users as MutableLiveData<List<UserDetail>>
    }

    private fun loadUsers(username: String) {
        val userArr = ArrayList<UserDetail>()
        val token = Constant.TOKEN
        val url = Constant.URL_DETAIL+username

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", token)
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val user = UserDetail()
                    user.id = responseObject.getInt("id")
                    user.username = responseObject.getString("login")
                    user.avatar = responseObject.getString("avatar_url")
                    user.name = responseObject.getString("name")
                    user.url = responseObject.getString("html_url")
                    user.location = responseObject.getString("location")

                    userArr.add(user)
                    users?.postValue(userArr)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun getUser() : MutableLiveData<List<UserDetail>>? {
        return users
    }
}