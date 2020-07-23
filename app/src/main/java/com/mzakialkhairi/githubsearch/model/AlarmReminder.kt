package com.mzakialkhairi.githubsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlarmReminder (
    var isTrue : Boolean = false
):Parcelable