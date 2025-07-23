package ru.stanise.animebrowsing.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("image_url")
    val imageUrl: String?,

    @SerialName("large_image_url")
    val largeImageUrl: String? = null
)


@Serializable
data class Images(
    val webp: Image
)
