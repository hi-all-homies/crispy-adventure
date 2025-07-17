package ru.stanise.animebrowsing.dto

import kotlinx.serialization.Serializable

@Serializable(with = StatusSerializer::class)
enum class Status(val rawValue: String) {
    AIRING("airing"),

    COMPLETE("complete"),

    UPCOMING("upcoming"),

    UNKNOWN("unknown")
}

val allowedStatuses: List<Status> = Status.entries.filter { it != Status.UNKNOWN }