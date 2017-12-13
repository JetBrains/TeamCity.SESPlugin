package jetbrains.buildServer.sesPlugin.util

import org.jmock.Expectations
import org.jmock.Mockery
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
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

fun Any.getResource(name: String): TestResource {
    val stream = ClassLoader.getSystemResourceAsStream(this::class.qualifiedName?.replace('.', '/') + "_" + name) ?: throw IOException(this::class.qualifiedName + "_" + name + " not found")
    return TestResource(stream)
}

class TestResource(private val stream: InputStream) {
    fun asString(): String {
        return stream.readBytes().toString(Charset.forName("UTF-8"))
    }
}