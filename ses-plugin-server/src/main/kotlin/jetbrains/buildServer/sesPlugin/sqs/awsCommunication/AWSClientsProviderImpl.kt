/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.sesPlugin.sqs.awsCommunication

import jetbrains.buildServer.sesPlugin.teamcity.SQSBean
import jetbrains.buildServer.util.amazon.AWSCommonParams

class AWSClientsProviderImpl : AWSClientsProvider {
    override fun <T> withClient(bean: SQSBean, func: SQSAWSClients.() -> T): T {
        return AWSCommonParams.withAWSClients<T, Exception>(bean.toMap()) { awsClients -> func.invoke(SQSAWSClientsImpl(awsClients)) }
    }
}