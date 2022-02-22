package com.example.limeapp.model

import java.io.Serializable

data class Channel(
    val cdn: String,
    val current: Current?,
    val id: Int,
    val image: String,
    val name_ru: String,
    val url: String
) : Serializable