package com.subin.cleanbookstore.presentation.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute {
    @Serializable
    data object Search : NavRoute

    @SuppressLint("UnsafeOptInUsageError")
    @Serializable
    data class Detail(val bookId: String) : NavRoute

    @Serializable
    data object Bookmark : NavRoute
}