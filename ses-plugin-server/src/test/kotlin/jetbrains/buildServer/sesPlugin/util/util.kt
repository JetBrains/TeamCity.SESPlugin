package jetbrains.buildServer.sesPlugin.util

import org.jmock.Expectations
import org.jmock.Mockery
import org.jmock.internal.Cardinality
import org.jmock.internal.InvocationExpectation
import org.jmock.internal.matcher.MethodNameMatcher
import org.jmock.internal.matcher.MockObjectMatcher
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

fun Mockery.check(action: Expectations.() -> Unit) {
    val expectations = Expectations()
    expectations.apply(action)
    this.checking(expectations)
}

fun mocking(action: Mockery.() -> Unit) {
    val mockery = Mockery()

    mockery.apply(action)
    mockery.assertIsSatisfied()
}

fun Mockery.invocation(action: InvocationExpectation.() -> Unit) {
    this.addExpectation(InvocationExpectation().apply(action))
}

fun InvocationExpectation.on(obj: Any) = setObjectMatcher(MockObjectMatcher(obj))

fun name(meth: KFunction<*>): MethodNameMatcher = MethodNameMatcher(meth.name)
fun InvocationExpectation.func(meth: KFunction<*>) = setMethodMatcher(MethodNameMatcher(meth.name))
fun InvocationExpectation.count(count: Int) = setCardinality(Cardinality.exactly(count))

fun <T : Any> Mockery.mock(clazz: KClass<T>, name: String? = null): T {
    return if (name != null) this.mock(clazz.java, name) else this.mock(clazz.java)
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