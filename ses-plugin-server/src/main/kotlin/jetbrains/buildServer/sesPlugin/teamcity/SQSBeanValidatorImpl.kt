

package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.data.SQSBeanValidatorResult
import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.util.amazon.AWSCommonParams

class SQSBeanValidatorImpl : SQSBeanValidator {
    override fun validate(bean: SQSBean): SQSBeanValidatorResult {
        val list = ArrayList<String>()
        if (bean.accountId.isEmpty()) {
            list.add(Constants.ACCOUNT_ID_PARAM)
        }
        if (bean.queueName.isEmpty()) {
            list.add(Constants.QUEUE_NAME_PARAM)
        }

        val map = bean.toMap()
//        if (map[AWSCommonParams.ENVIRONMENT_NAME_PARAM] == AWSCommonParams.ENVIRONMENT_TYPE_CUSTOM) {
        if (map[AWSCommonParams.REGION_NAME_PARAM].isNullOrEmpty()) {
            list.add(AWSCommonParams.REGION_NAME_PARAM)
        }
//            if (map[AWSCommonParams.SERVICE_ENDPOINT_PARAM].isNullOrEmpty()) {
//                list.add(AWSCommonParams.SERVICE_ENDPOINT_PARAM)
//            }
//        }
        val type = map[AWSCommonParams.CREDENTIALS_TYPE_PARAM]
        if (type.isNullOrEmpty()) {
            list.add(AWSCommonParams.CREDENTIALS_TYPE_PARAM)
        } else {
            if (type == AWSCommonParams.TEMP_CREDENTIALS_OPTION) {
                if (map[AWSCommonParams.IAM_ROLE_ARN_PARAM].isNullOrEmpty()) {
                    list.add(AWSCommonParams.IAM_ROLE_ARN_PARAM)
                }
                if (map[AWSCommonParams.EXTERNAL_ID_PARAM].isNullOrEmpty()) {
                    list.add(AWSCommonParams.EXTERNAL_ID_PARAM)
                }
            }
        }
        if (map[AWSCommonParams.USE_DEFAULT_CREDENTIAL_PROVIDER_CHAIN_PARAM]?.toBoolean() != true) {
            if (map[AWSCommonParams.ACCESS_KEY_ID_PARAM].isNullOrEmpty()) {
                list.add(AWSCommonParams.ACCESS_KEY_ID_PARAM)
            }
            if (map[AWSCommonParams.SECURE_SECRET_ACCESS_KEY_PARAM].isNullOrEmpty()) {
                list.add(AWSCommonParams.SECRET_ACCESS_KEY_PARAM)
            }
        }

        return SQSBeanValidatorResult(list.isEmpty(), if (list.isEmpty()) "correct" else "All mandatory fields should be filled", list)
    }

}