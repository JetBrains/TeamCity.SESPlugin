

package jetbrains.buildServer.sesPlugin.util

import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.BDDAssertions
import org.hamcrest.Matcher
import org.jmock.Expectations
import org.jmock.Mockery
import org.jmock.api.Action
import org.jmock.internal.Cardinality
import org.jmock.internal.InvocationExpectation
import org.jmock.internal.matcher.MethodMatcher
import org.jmock.internal.matcher.MethodNameMatcher
import org.jmock.internal.matcher.MockObjectMatcher
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

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
    val invocationExpectation = InvocationExpectation()
    invocationExpectation.count(1)
    this.addExpectation(invocationExpectation.apply(action))
}

fun Mockery.invocation(meth: KFunction<*>, action: InvocationExpectation.() -> Unit) {
    val invocationExpectation = InvocationExpectation()
    invocationExpectation.count(1)
    invocationExpectation.setMethodMatcher(MethodMatcher(meth.javaMethod))
    this.addExpectation(invocationExpectation.apply(action))
}

fun InvocationExpectation.on(obj: Any) = setObjectMatcher(MockObjectMatcher(obj))

fun InvocationExpectation.func(name: String) = setMethodMatcher(MethodNameMatcher(name))
fun InvocationExpectation.func(meth: KFunction<*>) = setMethodMatcher(MethodMatcher(meth.javaMethod))
fun InvocationExpectation.count(count: Int) = setCardinality(Cardinality.exactly(count))
fun InvocationExpectation.count(cardinality: Cardinality) = setCardinality(cardinality)
fun InvocationExpectation.will(returnValue: Action) = setAction(returnValue)
fun InvocationExpectation.with(parametersMatcher: Matcher<Array<Any>>) {
    setParametersMatcher(parametersMatcher)
}

fun then(action: () -> Unit): AbstractThrowableAssert<*, out Throwable> {
    return try {
        action.invoke()

        BDDAssertions.then(null as Exception?)
    } catch (e: Exception) {
        BDDAssertions.then(e)
    }
}

fun <T : Any> Mockery.mock(clazz: KClass<T>): T {
    return this.mock(clazz.java)
}

fun <T : Any> Mockery.mock(clazz: KClass<T>, name: String): T {
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