package com.omarzg94.mytvshows.data.model

data class Show(
    val id: Int,
    val name: String,
    val summary: String?,
    val image: Image?
)

data class Image(
    val medium: String,
    val original: String
)