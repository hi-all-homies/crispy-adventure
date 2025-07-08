package ru.stanise.animebrowsing.adapters

import com.apollographql.apollo.api.Adapter
import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.json.JsonReader
import com.apollographql.apollo.api.json.JsonWriter
import com.apollographql.apollo.api.json.writeAny
import kotlin.math.abs

object PositiveIntAdapter : Adapter<Int> {
    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): Int {
        return reader.nextInt()
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: Int) {
        val positiveInt = if (value != 0) abs(value) else 1
        writer.writeAny(positiveInt)
    }
}