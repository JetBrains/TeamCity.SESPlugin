package jetbrains.buildServer.sesPlugin.endPoint.validator

import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.NotificationRequestData
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.RequestData
import jetbrains.buildServer.sesPlugin.endPoint.dataHolders.SubscriptionRequestData
import jetbrains.buildServer.util.HTTPRequestBuilder
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.security.Signature
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.CompletableFuture

class RequestDataValidatorV1Impl : RequestDataValidator {
    // todo spring wiring
    lateinit var requestHandler: HTTPRequestBuilder.RequestHandler

    override fun validate(data: RequestData): CompletableFuture<RequestDataValidator.ValidationResult> {
        val result: CompletableFuture<RequestDataValidator.ValidationResult> = CompletableFuture()

        try {
            val request = HTTPRequestBuilder(data.signingCertURL)
                    .withMethod("GET")
                    .onException {
                        result.complete(RequestDataValidator.ValidationResult(false, it))
                    }
                    .onErrorResponse { code, _ ->
                        result.complete(RequestDataValidator.ValidationResult(false, IncorrectHttpResponseException("Incorrect response code $code")))
                    }
                    .onSuccess {
                        try {
                            val inputStream: InputStream = it.byteInputStream(Charset.forName("UTF-8"))
                            val cf = CertificateFactory.getInstance("X.509")
                            val cert = cf.generateCertificate(inputStream) as X509Certificate
                            inputStream.close()

                            val sig = Signature.getInstance("SHA1withRSA")
                            sig.initVerify(cert.publicKey)
                            sig.update(bytesToSign(data))
                            val verify = sig.verify(Base64.getDecoder().decode(data.signature))

                            result.complete(RequestDataValidator.ValidationResult(verify))
                        } catch (ex: Exception) {
                            result.complete(RequestDataValidator.ValidationResult(false, ex))
                        }
                    }
                    .build()

            requestHandler.doRequest(request)

        } catch (ex: Exception) {
            result.complete(RequestDataValidator.ValidationResult(false, ex))
        } finally {
            return result
        }
    }

    private fun bytesToSign(data: RequestData): ByteArray {
        return when (data.type) {
            "Notification" -> notificationBytesToSign(data as NotificationRequestData)
            "SubscriptionConfirmation", "UnsubscribeConfirmation" -> subscriptionBytesToSign(data as SubscriptionRequestData)
            else -> throw IllegalArgumentException("Unknown request type")
        }
    }

    private fun notificationBytesToSign(data: NotificationRequestData): ByteArray {
        with(data) {
            return ("Message\n" +
                    "$message\n" +
                    "MessageId\n" +
                    "$messageId\n" +
                    "Subject\n" +
                    "$subject\n" +
                    "Timestamp\n" +
                    "$timestamp\n" +
                    "TopicArn\n" +
                    "$topicArn\n" +
                    "Type\n" +
                    "$type\n").toByteArray(Charset.forName("UTF-8"));
        }
    }

    private fun subscriptionBytesToSign(data: SubscriptionRequestData): ByteArray {
        with(data) {
            val sb = StringBuilder()
            sb.append("Message\n")
            sb.append(message).append("\n")
            sb.append("MessageId\n")
            sb.append(messageId).append("\n")
            sb.append("SubscribeURL\n")
            sb.append(subscribeURL).append("\n")
            sb.append("Timestamp\n")
            sb.append(timestamp).append("\n")
            sb.append("Token\n")
            sb.append(token).append("\n")
            sb.append("TopicArn\n")
            sb.append(topicArn).append("\n")
            sb.append("Type\n")
            sb.append(type).append("\n")
            return sb.toString().toByteArray(Charset.forName("UTF-8"))
        }
    }
}

class IncorrectHttpResponseException(message: String?) : IOException(message)