package com.mzakialkhairi.githubsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite (
    var id : Int = 0,
    var username : String? = null,
    var avatar : String? = null
) : Parcelable