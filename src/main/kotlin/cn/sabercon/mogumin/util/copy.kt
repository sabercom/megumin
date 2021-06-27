package cn.sabercon.mogumin.util

import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*

inline fun <reified T : Any> convertFrom(source: Any, vararg otherProperties: Pair<KProperty1<T, *>, Any?>): T {
    val sourceProperties = source::class.memberProperties.map { it.name to it.getter.call(source) }
    val totalProperties = sourceProperties + otherProperties.map { it.first.name to it.second }

    val constructor = T::class.primaryConstructor!!
    return totalProperties.toMap().filterKeys { constructor.valueParameters.any { p -> p.name!! == it } }
        .mapKeys { constructor.valueParameters.first { p -> p.name!! == it.key } }
        .let { constructor.callBy(it) }
}

inline fun <reified T : Any> copyFrom(target: T, source: Any, vararg otherProperties: Pair<KProperty1<T, *>, Any?>): T {
    val sourceProperties = source::class.memberProperties.map { it.name to it.getter.call(source) }
    val totalProperties = sourceProperties + otherProperties.map { it.first.name to it.second }

    @Suppress("UNCHECKED_CAST")
    val copyMethod = T::class.memberFunctions.first { it.name == "copy" } as KFunction<T>
    return totalProperties.toMap().filterKeys { copyMethod.valueParameters.any { p -> p.name!! == it } }
        .mapKeys { copyMethod.valueParameters.first { p -> p.name!! == it.key } }
        .plus(copyMethod.instanceParameter!! to target)
        .let(copyMethod::callBy)
}