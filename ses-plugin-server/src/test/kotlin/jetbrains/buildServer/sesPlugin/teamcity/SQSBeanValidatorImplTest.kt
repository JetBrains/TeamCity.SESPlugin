package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.sesPlugin.util.check
import jetbrains.buildServer.sesPlugin.util.mock
import jetbrains.buildServer.sesPlugin.util.mocking
import jetbrains.buildServer.util.amazon.AWSCommonParams
import org.assertj.core.api.BDDAssertions
import org.jmock.Expectations
import org.testng.annotations.Test

class SQSBeanValidatorImplTest {
    @Test
    fun test() {
        mocking {
            val bean = mock(SQSBean::class)

            check {
                one(bean).accountId; will(Expectations.returnValue(""))
                one(bean).queueName; will(Expectations.returnValue(""))
                one(bean).toMap(); will(Expectations.returnValue(emptyMap<String, String>()))
            }

            BDDAssertions.then(SQSBeanValidatorImpl().validate(bean).errorFields)
                    .containsExactly(Constants.ACCOUNT_ID_PARAM, Constants.QUEUE_NAME_PARAM, AWSCommonParams.REGION_NAME_PARAM, AWSCommonParams.CREDENTIALS_TYPE_PARAM, AWSCommonParams.ACCESS_KEY_ID_PARAM, AWSCommonParams.SECRET_ACCESS_KEY_PARAM)
        }
    }

    @Test
    fun testTempCredentials() {
        mocking {
            val bean = mock(SQSBean::class)

            check {
                one(bean).accountId; will(Expectations.returnValue(""))
                one(bean).queueName; will(Expectations.returnValue(""))
                one(bean).toMap(); will(Expectations.returnValue(mapOf(AWSCommonParams.CREDENTIALS_TYPE_PARAM to AWSCommonParams.TEMP_CREDENTIALS_OPTION)))
            }

            BDDAssertions.then(SQSBeanValidatorImpl().validate(bean).errorFields)
                    .containsExactly(Constants.ACCOUNT_ID_PARAM, Constants.QUEUE_NAME_PARAM, AWSCommonParams.REGION_NAME_PARAM, AWSCommonParams.IAM_ROLE_ARN_PARAM, AWSCommonParams.EXTERNAL_ID_PARAM, AWSCommonParams.ACCESS_KEY_ID_PARAM, AWSCommonParams.SECRET_ACCESS_KEY_PARAM)
        }
    }

    @Test
    fun testDefaultChain() {
        mocking {
            val bean = mock(SQSBean::class)

            check {
                one(bean).accountId; will(Expectations.returnValue(""))
                one(bean).queueName; will(Expectations.returnValue(""))
                one(bean).toMap(); will(Expectations.returnValue(mapOf(AWSCommonParams.USE_DEFAULT_CREDENTIAL_PROVIDER_CHAIN_PARAM to true.toString())))
            }

            BDDAssertions.then(SQSBeanValidatorImpl().validate(bean).errorFields)
                    .containsExactly(Constants.ACCOUNT_ID_PARAM, Constants.QUEUE_NAME_PARAM, AWSCommonParams.REGION_NAME_PARAM, AWSCommonParams.CREDENTIALS_TYPE_PARAM)
        }
    }
}