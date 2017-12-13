package jetbrains.buildServer.sesPlugin.util

import org.jmock.Expectations
import org.jmock.Mockery
import kotlin.reflect.KClass

fun Mockery.check(action: Expectations.() -> Unit) {
    val expectations = Expectations()
    expectations.apply(action)
    this.checking(expectations)
}

fun mocking(action: Mockery.() -> Unit) {
    val mockery = Mockery()
    try {
        mockery.apply(action)
    } finally {
        mockery.assertIsSatisfied()
    }
}

fun <T : Any> Mockery.mock(clazz: KClass<T>, name: String? = null): T {
    return this.mock(clazz.java, name)
}