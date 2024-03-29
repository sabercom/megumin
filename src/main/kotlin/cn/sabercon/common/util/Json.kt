package cn.sabercon.common.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue

val JSON: JsonMapper = jacksonMapperBuilder()
    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .addModules(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .build()

fun Any.toJson(): String = JSON.writeValueAsString(this)

fun String.toJsonNode(): JsonNode = JSON.readTree(this)

inline fun <reified T : Any> String.toJsonObject(): T = JSON.readValue(this)

inline fun <reified T : Any> JsonNode.toObject(): T = JSON.convertValue(this)
