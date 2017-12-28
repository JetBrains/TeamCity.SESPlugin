package jetbrains.buildServer.sesPlugin.teamcity

import jetbrains.buildServer.sesPlugin.data.SQSBeanValidatorResult
import jetbrains.buildServer.sesPlugin.teamcity.util.Constants
import jetbrains.buildServer.util.amazon.AWSCommonParams

class SQSBeanValidatorImpl : SQSBeanValidator {
    override fun validate(bean: SQSBean): SQSBeanValidatorResult {
        val list = ArrayList<String>()
        if (bean.accountId.isEmpty()) {
            list.add(Constants.ACCOUNT_ID_LABEL)
        }
        if (bean.queueName.isEmpty()) {
            list.add(Constants.QUEUE_NAME_LABEL)
        }

        val map = bean.toMap()
        if (map[AWSCommonParams.REGION_NAME_PARAM].isNullOrEmpty()) {
            list.add(AWSCommonParams.REGION_NAME_LABEL)
        }
        val type = map[AWSCommonParams.CREDENTIALS_TYPE_PARAM]
        if (type.isNullOrEmpty()) {
            list.add(AWSCommonParams.CREDENTIALS_TYPE_LABEL)
        } else {
            when (type) {
                AWSCommonParams.ACCESS_KEYS_OPTION -> {
                    if (map[AWSCommonParams.ACCESS_KEY_ID_PARAM].isNullOrEmpty()) {
                        list.add(AWSCommonParams.ACCESS_KEY_ID_LABEL)
                    }
                    if (map[AWSCommonParams.SECURE_SECRET_ACCESS_KEY_PARAM].isNullOrEmpty()) {
                        list.add(AWSCommonParams.SECRET_ACCESS_KEY_LABEL)
                    }
                }
                AWSCommonParams.TEMP_CREDENTIALS_OPTION -> {
                    if (map[AWSCommonParams.IAM_ROLE_ARN_PARAM].isNullOrEmpty()) {
                        list.add(AWSCommonParams.IAM_ROLE_ARN_LABEL)
                    }
                    if (map[AWSCommonParams.EXTERNAL_ID_PARAM].isNullOrEmpty()) {
                        list.add(AWSCommonParams.EXTERNAL_ID_LABEL)
                    }
                }
            }
        }

        return SQSBeanValidatorResult(list.isEmpty(), if (list.isEmpty()) "correct" else "All mandatory fields should be filled", list)
    }

}