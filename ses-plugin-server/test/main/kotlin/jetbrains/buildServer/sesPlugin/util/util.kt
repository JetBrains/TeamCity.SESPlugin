package jetbrains.buildServer.sesPlugin.util

import org.jmock.Expectations
import org.jmock.Mockery

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