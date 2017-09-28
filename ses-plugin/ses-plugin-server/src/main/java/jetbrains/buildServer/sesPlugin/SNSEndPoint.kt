package jetbrains.buildServer.sesPlugin

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.util.HTTPRequestBuilder
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.security.Signature
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EndPoint(server: SBuildServer) : BaseController(server) {
    private val AMAZON_TYPE_HEADER = "x-amz-sns-message-type"

    private val myRequestDataValidatorFactory: RequestDataValidatorFactory
        get() {
            return RequestDataValidatorFactoryImpl()
        }
    private val myRequestHandlerFactory: RequestHandlerFactory
        get() {
            return RequestHandlerFactoryImpl()
        }

    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        if (!acceptedContentLength(request.contentLength)) {
            return null
        }

        val inputStream = request.inputStream

        val data: ResponseDataHolder
        try {
            val jsonResponseDataParser = JsonResponseDataParser()
            data = jsonResponseDataParser.parse(request.getHeader(AMAZON_TYPE_HEADER), inputStream)
        } catch (ex: JsonSyntaxException) {
            return null
        } catch (ex: JsonIOException) {
            return null
        }

        val validator = myRequestDataValidatorFactory.getValidator(data)
        val validate = validator.validate(data)

        validate.thenAccept {
            if (it.isCorrect) {
                val handler = myRequestHandlerFactory.getHandler(data)
                handler.handle()
            } else {

            }
        }

        return null
    }

    private fun acceptedContentLength(contentLength: Int): Boolean {
        return true
    }
}

interface RequestHandlerFactory {
    fun getHandler(data: ResponseDataHolder): RequestHandler
}

interface RequestHandler {
    fun handle()
}

interface RequestDataValidator {
    fun validate(data: ResponseDataHolder): CompletableFuture<ValidationResult>
}

interface RequestDataValidatorFactory {
    fun getValidator(data: ResponseDataHolder): RequestDataValidator
}

data class ValidationResult(val isCorrect: Boolean, val exception: Exception? = null)

interface ResponseDataHolder {
    val type: String
    val messageId: String
    val topicArn: String
    val message: String
    val timestamp: String
    val signatureVersion: String
    val signature: String
    val signingCertURL: String
}

interface SubscriptionResponseDataHolder : ResponseDataHolder {
    val token: String
    val subscribeURL: String
}

class SubscribeResponseDataHolder(
        @SerializedName("Type") override val type: String,
        @SerializedName("MessageId") override val messageId: String,
        @SerializedName("TopicArn") override val topicArn: String,
        @SerializedName("Message") override val message: String,
        @SerializedName("Timestamp") override val timestamp: String,
        @SerializedName("SignatureVersion") override val signatureVersion: String,
        @SerializedName("Signature") override val signature: String,
        @SerializedName("SigningCertURL") override val signingCertURL: String,
        @SerializedName("Token") override val token: String,
        @SerializedName("SubscribeURL") override val subscribeURL: String) : SubscriptionResponseDataHolder {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubscribeResponseDataHolder

        if (type != other.type) return false
        if (messageId != other.messageId) return false
        if (topicArn != other.topicArn) return false
        if (message != other.message) return false
        if (timestamp != other.timestamp) return false
        if (signatureVersion != other.signatureVersion) return false
        if (signature != other.signature) return false
        if (signingCertURL != other.signingCertURL) return false
        if (token != other.token) return false
        if (subscribeURL != other.subscribeURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + messageId.hashCode()
        result = 31 * result + topicArn.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + signatureVersion.hashCode()
        result = 31 * result + signature.hashCode()
        result = 31 * result + signingCertURL.hashCode()
        result = 31 * result + token.hashCode()
        result = 31 * result + subscribeURL.hashCode()
        return result
    }
}

class UnsubscribeResponseDataHolder(@SerializedName("Type") override val type: String,
                                    @SerializedName("MessageId") override val messageId: String,
                                    @SerializedName("TopicArn") override val topicArn: String,
                                    @SerializedName("Message") override val message: String,
                                    @SerializedName("Timestamp") override val timestamp: String,
                                    @SerializedName("SignatureVersion") override val signatureVersion: String,
                                    @SerializedName("Signature") override val signature: String,
                                    @SerializedName("SigningCertURL") override val signingCertURL: String,
                                    @SerializedName("Token") override val token: String,
                                    @SerializedName("SubscribeURL") override val subscribeURL: String) : SubscriptionResponseDataHolder {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnsubscribeResponseDataHolder

        if (type != other.type) return false
        if (messageId != other.messageId) return false
        if (topicArn != other.topicArn) return false
        if (message != other.message) return false
        if (timestamp != other.timestamp) return false
        if (signatureVersion != other.signatureVersion) return false
        if (signature != other.signature) return false
        if (signingCertURL != other.signingCertURL) return false
        if (token != other.token) return false
        if (subscribeURL != other.subscribeURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + messageId.hashCode()
        result = 31 * result + topicArn.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + signatureVersion.hashCode()
        result = 31 * result + signature.hashCode()
        result = 31 * result + signingCertURL.hashCode()
        result = 31 * result + token.hashCode()
        result = 31 * result + subscribeURL.hashCode()
        return result
    }
}

class NotificationResponseDataHolder(@SerializedName("Type") override val type: String,
                                     @SerializedName("MessageId") override val messageId: String,
                                     @SerializedName("TopicArn") override val topicArn: String,
                                     @SerializedName("Message") override val message: String,
                                     @SerializedName("Timestamp") override val timestamp: String,
                                     @SerializedName("SignatureVersion") override val signatureVersion: String,
                                     @SerializedName("Signature") override val signature: String,
                                     @SerializedName("SigningCertURL") override val signingCertURL: String,
                                     @SerializedName("Subject") val subject: String,
                                     @SerializedName("UnsubscribeURL") val unsubscribeURL: String) : ResponseDataHolder {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificationResponseDataHolder

        if (type != other.type) return false
        if (messageId != other.messageId) return false
        if (topicArn != other.topicArn) return false
        if (message != other.message) return false
        if (timestamp != other.timestamp) return false
        if (signatureVersion != other.signatureVersion) return false
        if (signature != other.signature) return false
        if (signingCertURL != other.signingCertURL) return false
        if (subject != other.subject) return false
        if (unsubscribeURL != other.unsubscribeURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + messageId.hashCode()
        result = 31 * result + topicArn.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + signatureVersion.hashCode()
        result = 31 * result + signature.hashCode()
        result = 31 * result + signingCertURL.hashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + unsubscribeURL.hashCode()
        return result
    }
}


class RequestHandlerFactoryImpl : RequestHandlerFactory {
    override fun getHandler(data: ResponseDataHolder): RequestHandler {
        return when (data) {
            is SubscribeResponseDataHolder -> SubscribeRequestHandler(data)
            is UnsubscribeResponseDataHolder -> UnsubscribeRequestHandler(data)
            is NotificationResponseDataHolder -> NotificationRequestHandler(data)
            else -> throw IllegalArgumentException("Unknown request type " + data.type)
        }
    }
}

class NotificationRequestHandler(private val data: NotificationResponseDataHolder) : RequestHandler {
    override fun handle() {
        // todo need infrastructure
    }
}

class UnsubscribeRequestHandler(private val data: UnsubscribeResponseDataHolder) : RequestHandler {
    override fun handle() {
    }
}

class SubscribeRequestHandler(private val data: SubscribeResponseDataHolder) : RequestHandler {
    // todo spring wiring
    lateinit var requestHandler: HTTPRequestBuilder.RequestHandler

    override fun handle() {
        val request = HTTPRequestBuilder(data.subscribeURL)
                .withMethod("GET")
                .onException(this::onException)
                .onErrorResponse(this::onErrorResponse)
                .onSuccess(this::onSuccess)
                .build()

        requestHandler.doRequest(request)
    }

    private fun onException(ex: java.lang.Exception) {

    }

    private fun onErrorResponse(code: Int, data: String) {

    }

    private fun onSuccess(data: String) {
        // todo Process the return value to ensure the endpoint is subscribed
    }
}

class IncorrectHttpResponseException(message: String?) : IOException(message)

class RequestDataValidatorV1Impl : RequestDataValidator {
    // todo spring wiring
    lateinit var requestHandler: HTTPRequestBuilder.RequestHandler

    override fun validate(data: ResponseDataHolder): CompletableFuture<ValidationResult> {
        val result: CompletableFuture<ValidationResult> = CompletableFuture()

        try {
            val request = HTTPRequestBuilder(data.signingCertURL)
                    .withMethod("GET")
                    .onException {
                        result.complete(ValidationResult(false, it))
                    }
                    .onErrorResponse { code, _ ->
                        result.complete(ValidationResult(false, IncorrectHttpResponseException("Incorrect response code $code")))
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

                            result.complete(ValidationResult(verify))
                        } catch (ex: Exception) {
                            result.complete(ValidationResult(false, ex))
                        }
                    }
                    .build()

            requestHandler.doRequest(request)

        } catch (ex: Exception) {
            result.complete(ValidationResult(false, ex))
        } finally {
            return result
        }
    }

    private fun bytesToSign(data: ResponseDataHolder): ByteArray {
        return when (data.type) {
            "Notification" -> notificationBytesToSign(data as NotificationResponseDataHolder)
            "SubscriptionConfirmation", "UnsubscribeConfirmation" -> subscriptionBytesToSign(data as SubscriptionResponseDataHolder)
            else -> throw IllegalArgumentException("Unknown request type")
        }
    }

    private fun notificationBytesToSign(data: NotificationResponseDataHolder): ByteArray {
        with(data) {
            val sb = StringBuilder()
            sb.append("Message\n")
            sb.append(message).append("\n")
            sb.append("MessageId\n")
            sb.append(messageId).append("\n")
            sb.append("Subject\n")
            sb.append(subject).append("\n")
            sb.append("Timestamp\n")
            sb.append(timestamp).append("\n")
            sb.append("TopicArn\n")
            sb.append(topicArn).append("\n")
            sb.append("Type\n")
            sb.append(type).append("\n")
            return sb.toString().toByteArray(Charset.forName("UTF-8"))
        }
    }

    private fun subscriptionBytesToSign(data: SubscriptionResponseDataHolder): ByteArray {
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

class RequestDataValidatorFactoryImpl : RequestDataValidatorFactory {
    override fun getValidator(data: ResponseDataHolder): RequestDataValidator {
        when (data.signatureVersion) {
            "1" -> {
                return RequestDataValidatorV1Impl()
            }
            else -> {
                throw SecurityException("Unknown signature version: ${data.signatureVersion}")
            }
        }
    }
}

