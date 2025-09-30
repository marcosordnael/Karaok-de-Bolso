package com.marcosdev.karaokdebolso.model

data class Song (
    val id: Int = 0,
    val title: String,
    val artist: String? = null,
    val link: String? = null
)

