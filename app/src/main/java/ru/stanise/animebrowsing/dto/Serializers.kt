package ru.stanise.animebrowsing.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object AnimeTypeSerializer : KSerializer<AnimeType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("AnimeType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AnimeType) {
        encoder.encodeString(value.rawValue)
    }

    override fun deserialize(decoder: Decoder): AnimeType {
        val value = decoder.decodeString()
        return AnimeType.entries.firstOrNull {
            it.name.equals(value, ignoreCase = true) ||
                    it.rawValue.equals(value, ignoreCase = true)
        } ?: AnimeType.UNKNOWN
    }
}

object StatusSerializer : KSerializer<Status> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Status", PrimitiveKind.STRING)

    private val serialNameMap = mapOf(
        "Currently Airing" to Status.AIRING,
        "Finished Airing" to Status.COMPLETE,
        "Not yet aired" to Status.UPCOMING
    )

    override fun deserialize(decoder: Decoder): Status {
        val value = decoder.decodeString()
        return serialNameMap[value] ?: Status.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: Status) {
        encoder.encodeString(
            serialNameMap.entries.find { it.value == value }?.value?.rawValue ?: "unknown"
        )
    }
}


object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OffsetDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OffsetDateTime {
        val text = decoder.decodeString()
        return OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }
}