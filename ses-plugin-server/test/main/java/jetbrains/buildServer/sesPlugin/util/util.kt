package jetbrains.buildServer.sesPlugin.util

import org.jmock.Expectations
import org.jmock.Mockery

fun Mockery.check(action: Expectations.() -> Unit) {
    val expectations = Expectations()
    expectations.apply(action)
    this.checking(expectations)
}