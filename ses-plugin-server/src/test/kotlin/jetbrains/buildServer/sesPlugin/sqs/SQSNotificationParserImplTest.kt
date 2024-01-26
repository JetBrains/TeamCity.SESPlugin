

package jetbrains.buildServer.sesPlugin.sqs

import jetbrains.buildServer.sesPlugin.util.getResource
import org.assertj.core.api.BDDAssertions
import org.testng.annotations.Test

class SQSNotificationParserImplTest {
    @Test
    fun testParse() {
        val data = getResource("testParse").asString()

        val parsed = SQSNotificationParserImpl().parse(data)
        BDDAssertions.then(parsed.exception).isNull()
        BDDAssertions.then(parsed.result).isNotNull()

        BDDAssertions.then(parsed.result!!.Type).isEqualTo("Notification")
        BDDAssertions.then(parsed.result!!.MessageId).isEqualTo("ace76e28-4a8d-5b11-a516-3c5964f9d54d")
        BDDAssertions.then(parsed.result!!.TopicArn).isEqualTo("arn:aws:sns:us-east-1:172801717669:teamcity-ses-nonprod-bounces")
        BDDAssertions.then(parsed.result!!.Subject).isEqualTo("Amazon SES Email Event Notification")
        BDDAssertions.then(parsed.result!!.Timestamp).isEqualTo("2017-10-30T13:42:12.183Z")
        BDDAssertions.then(parsed.result!!.SignatureVersion).isEqualTo("1")
        BDDAssertions.then(parsed.result!!.Signature).isEqualTo("B2YbtxYSMAw+ui5y1iGGZ56mbAFQ7J/WIofZ415NVM2knUk2V4TFM9Ri/OqY3trv9a9+86SqtE/3K7UT8iVYyLhkFre/rzpwfoi+mxwY6k/4YDerYjpbMMGoaY8VnW1iWo4G3494DUS3IUjAe5Cmm6ZS3OwNeGE1GYxMVtD51Ai3Qe5mVvZ0oOb1OMgfnO/uM9ylo7jW8sa5592qbGJg+W8TQYY3EsKgJiwLZ5DaHMIxqkJoDjdIRzmS3oaOUEGaQhN8T2hrOHk5/Un6Ce5TdFQPvitpJpEIR3xWMPNKregWZyFR7a3foEXFUOZ8j0cloIYrzSUAoflxGNKvitQ8vA==")
        BDDAssertions.then(parsed.result!!.SigningCertURL).isEqualTo("https://sns.us-east-1.amazonaws.com/SimpleNotificationService-433026a4050d206028891664da859041.pem")
        BDDAssertions.then(parsed.result!!.UnsubscribeURL).isEqualTo("https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:172801717669:teamcity-ses-nonprod-bounces:ce4476d2-ec34-4167-8a52-941750ef1cc6")
        BDDAssertions.then(parsed.result!!.Message).isNotNull()
    }

    @Test
    fun testNotAllFields() {
        val data = getResource("testParse").asString()

        val obligatoryFields = listOf("Type", "MessageId", "TopicArn", "Subject", "Timestamp", "SignatureVersion", "Signature", "SigningCertURL", "Message")
        for (obligatoryField in obligatoryFields) {
            val parsed = SQSNotificationParserImpl().parse(data.replace(obligatoryField, "_"))
            BDDAssertions.then(parsed.exception).describedAs("Should return exception if obligatory field %s not found", obligatoryField).isNotNull()
        }

        val notObligatoryFields = listOf("UnsubscribeURL")
        for (notObligatoryField in notObligatoryFields) {
            val parsed = SQSNotificationParserImpl().parse(data.replace(notObligatoryField, "_"))
            BDDAssertions.then(parsed.exception).describedAs("Should not return exception if not obligatory field %s not found", notObligatoryField).isNull()
            BDDAssertions.then(parsed.result).describedAs("Should not return exception if not obligatory field %s not found", notObligatoryField).isNotNull()
        }
    }
}