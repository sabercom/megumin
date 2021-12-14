package cn.sabercon.common.util

import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*

/** CAUTION: reflection is really error-prone and ill-performance, so DO NOT use these functions unless you have to */


fun <T : Any?> Any.getProperty(name: String): T {
    @Suppress("UNCHECKED_CAST")
    return this::class.memberProperties.first { it.name == name }.getter.call(this) as T
}

fun <T : Any> T.copy(vararg properties: Pair<String, Any?>): T {
    @Suppress("UNCHECKED_CAST")
    val copyMethod = this::class.memberFunctions.first { it.name == "copy" } as KFunction<T>

    return properties.toMap()
        .mapKeys { copyMethod.findParameterByName(it.key)!! }
        .plus(copyMethod.instanceParameter!! to this)
        .let { copyMethod.callBy(it) }
}

fun <T : Any> T.mergeData(obj: Any, vararg properties: Pair<KProperty1<T, *>, Any?>): T {
    val sourceProperties = obj::class.memberProperties.map { it.name to it.getter.call(obj) }
    val otherProperties = properties.map { it.first.name to it.second }
    val totalProperties = sourceProperties + otherProperties

    return copy(*totalProperties.toTypedArray())
}

inline fun <reified T : Any> Any.convertData(vararg properties: Pair<KProperty1<T, *>, Any?>): T {
    val sourceProperties = this::class.memberProperties.map { it.name to it.getter.call(this) }
    val otherProperties = properties.map { it.first.name to it.second }
    val totalProperties = sourceProperties + otherProperties
    val constructor = T::class.primaryConstructor!!

    return totalProperties.toMap()
        .mapKeys { constructor.findParameterByName(it.key)!! }
        .let { constructor.callBy(it) }
}