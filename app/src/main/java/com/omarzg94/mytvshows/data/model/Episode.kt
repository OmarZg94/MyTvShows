package com.omarzg94.mytvshows.data.model

data class Episode(
    val id: Int,
    val name: String,
    val season: Int,
    val number: Int,
    val airdate: String,
    val airtime: String,
    val runtime: Int?,
    val show: Show
)
