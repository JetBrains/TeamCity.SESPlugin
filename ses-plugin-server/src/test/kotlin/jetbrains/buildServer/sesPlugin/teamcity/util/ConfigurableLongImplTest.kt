package jetbrains.buildServer.sesPlugin.teamcity.util

import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import org.assertj.core.api.BDDAssertions
import org.jmock.Expectations.returnValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class ConfigurableLongImplTest {
    @Test(dataProvider = "data")
    fun test(prop: Long, default: Long, min: Long, result: Long) {
        mocking {
            val properties = mock(TeamCityProperties::class)

            check {
                one(properties).getLong("key", default); will(returnValue(if (prop >= 0) prop else default))
            }

            val longImpl = ConfigurableLongImpl(properties, "key", default, min)
            BDDAssertions.then(longImpl.get()).describedAs("prop = [$prop], default = [$default], min = [$min], result = [$result]").isEqualTo(result)
        }
    }

    @DataProvider(name = "data")
    private fun data(): Array<Array<Long>> {
        return arrayOf(
                arrayOf(2L, 10L, 1L, 2L),
                arrayOf(2L, 10L, 3L, 3L),
                arrayOf(-1L, 2L, 1L, 2L),
                arrayOf(1L, 2L, 2L, 2L)
        )
    }
}