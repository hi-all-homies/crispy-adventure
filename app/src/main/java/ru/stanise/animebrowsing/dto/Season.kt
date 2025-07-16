package ru.stanise.animebrowsing.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Season(val rawValue: String) {
    @SerialName("summer")
    SUMMER("summer"),

    @SerialName("winter")
    WINTER("winter"),

    @SerialName("spring")
    SPRING("spring"),

    @SerialName("fall")
    FALL("fall")
}