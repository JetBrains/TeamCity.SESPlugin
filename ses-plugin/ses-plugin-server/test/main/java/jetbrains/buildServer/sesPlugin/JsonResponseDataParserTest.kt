package jetbrains.buildServer.sesPlugin

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.BDDAssertions.then
import org.junit.Test

class JsonResponseDataParserTest {
    @Test
    fun testParseSubscriptionConfirmation() {
        // note doubled escape in Message field for newline
        val parse = JsonResponseDataParser().parse(JsonResponseDataParser.SUBSCRIBE_TYPE, ("{\n" +
                "  \"Type\" : \"SubscriptionConfirmation\",\n" +
                "  \"MessageId\" : \"165545c9-2a5c-472c-8df2-7ff2be2b3b1b\",\n" +
                "  \"Token\" : \"2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-west-2:123456789012:MyTopic\",\n" +
                "  \"Message\" : \"You have chosen to subscribe to the topic arn:aws:sns:us-west-2:123456789012:MyTopic.\\\\nTo confirm the subscription, visit the SubscribeURL included in this message.\",\n" +
                "  \"SubscribeURL\" : \"https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                "  \"Timestamp\" : \"2012-04-26T20:45:04.751Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"EXAMPLEpH+DcEwjAPg8O9mY8dReBSwksfg2S7WKQcikcNKWLQjwu6A4VbeS0QHVCkhRS7fUQvi2egU3N858fiTDN6bkkOxYDVrY0Ad8L10Hs3zH81mtnPk5uvvolIC1CXGu43obcgFxeL3khZl8IKvO61GWB6jI9b5+gLPoBc1Q=\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem\"\n" +
                "  }").byteInputStream())

        then(parse).isEqualTo(SubscribeResponseDataHolder(
                "SubscriptionConfirmation",
                "165545c9-2a5c-472c-8df2-7ff2be2b3b1b",
                "arn:aws:sns:us-west-2:123456789012:MyTopic",
                "You have chosen to subscribe to the topic arn:aws:sns:us-west-2:123456789012:MyTopic.\\nTo confirm the subscription, visit the SubscribeURL included in this message.",
                "2012-04-26T20:45:04.751Z",
                "1",
                "EXAMPLEpH+DcEwjAPg8O9mY8dReBSwksfg2S7WKQcikcNKWLQjwu6A4VbeS0QHVCkhRS7fUQvi2egU3N858fiTDN6bkkOxYDVrY0Ad8L10Hs3zH81mtnPk5uvvolIC1CXGu43obcgFxeL3khZl8IKvO61GWB6jI9b5+gLPoBc1Q=",
                "https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem",
                "2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736",
                "https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736"))
    }

    @Test
    fun testParseUnsubscribe() {
        // note doubled escape in Message field for newline
        val parse = JsonResponseDataParser().parse(JsonResponseDataParser.UNSUBSCRIBE_TYPE, ("{\n" +
                "  \"Type\" : \"UnsubscribeConfirmation\",\n" +
                "  \"MessageId\" : \"47138184-6831-46b8-8f7c-afc488602d7d\",\n" +
                "  \"Token\" : \"2336412f37fb687f5d51e6e241d09c805a5a57b30d712f7948a98bac386edfe3e10314e873973b3e0a3c09119b722dedf2b5e31c59b13edbb26417c19f109351e6f2169efa9085ffe97e10535f4179ac1a03590b0f541f209c190f9ae23219ed6c470453e06c19b5ba9fcbb27daeb7c7\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-west-2:123456789012:MyTopic\",\n" +
                "  \"Message\" : \"You have chosen to deactivate subscription arn:aws:sns:us-west-2:123456789012:MyTopic:2bcfbf39-05c3-41de-beaa-fcfcc21c8f55.\\\\nTo cancel this operation and restore the subscription, visit the SubscribeURL included in this message.\",\n" +
                "  \"SubscribeURL\" : \"https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f7948a98bac386edfe3e10314e873973b3e0a3c09119b722dedf2b5e31c59b13edbb26417c19f109351e6f2169efa9085ffe97e10535f4179ac1a03590b0f541f209c190f9ae23219ed6c470453e06c19b5ba9fcbb27daeb7c7\",\n" +
                "  \"Timestamp\" : \"2012-04-26T20:06:41.581Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"EXAMPLEHXgJmXqnqsHTlqOCk7TIZsnk8zpJJoQbr8leD+8kAHcke3ClC4VPOvdpZo9s/vR9GOznKab6sjGxE8uwqDI9HwpDm8lGxSlFGuwCruWeecnt7MdJCNh0XK4XQCbtGoXB762ePJfaSWi9tYwzW65zAFU04WkNBkNsIf60=\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem\"\n" +
                "  }").byteInputStream())

        then(parse).isEqualTo(UnsubscribeResponseDataHolder(
                "UnsubscribeConfirmation",
                "47138184-6831-46b8-8f7c-afc488602d7d",
                "arn:aws:sns:us-west-2:123456789012:MyTopic",
                "You have chosen to deactivate subscription arn:aws:sns:us-west-2:123456789012:MyTopic:2bcfbf39-05c3-41de-beaa-fcfcc21c8f55.\\nTo cancel this operation and restore the subscription, visit the SubscribeURL included in this message.",
                "2012-04-26T20:06:41.581Z",
                "1",
                "EXAMPLEHXgJmXqnqsHTlqOCk7TIZsnk8zpJJoQbr8leD+8kAHcke3ClC4VPOvdpZo9s/vR9GOznKab6sjGxE8uwqDI9HwpDm8lGxSlFGuwCruWeecnt7MdJCNh0XK4XQCbtGoXB762ePJfaSWi9tYwzW65zAFU04WkNBkNsIf60=",
                "https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem",
                "2336412f37fb687f5d51e6e241d09c805a5a57b30d712f7948a98bac386edfe3e10314e873973b3e0a3c09119b722dedf2b5e31c59b13edbb26417c19f109351e6f2169efa9085ffe97e10535f4179ac1a03590b0f541f209c190f9ae23219ed6c470453e06c19b5ba9fcbb27daeb7c7",
                "https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f7948a98bac386edfe3e10314e873973b3e0a3c09119b722dedf2b5e31c59b13edbb26417c19f109351e6f2169efa9085ffe97e10535f4179ac1a03590b0f541f209c190f9ae23219ed6c470453e06c19b5ba9fcbb27daeb7c7"))
    }

    @Test
    fun testParseNotification() {
        // note doubled escape in Message field for newline
        val parse = JsonResponseDataParser().parse(JsonResponseDataParser.NOTIFICATION_TYPE, ("{\n" +
                "  \"Type\" : \"Notification\",\n" +
                "  \"MessageId\" : \"22b80b92-fdea-4c2c-8f9d-bdfb0c7bf324\",\n" +
                "  \"Token\" : \"2336412f37fb687f5d51e6e241d09c805a5a57b30d712f7948a98bac386edfe3e10314e873973b3e0a3c09119b722dedf2b5e31c59b13edbb26417c19f109351e6f2169efa9085ffe97e10535f4179ac1a03590b0f541f209c190f9ae23219ed6c470453e06c19b5ba9fcbb27daeb7c7\",\n" +
                "  \"TopicArn\" : \"arn:aws:sns:us-west-2:123456789012:MyTopic\",\n" +
                "  \"Subject\" : \"My First Message\",\n" +
                "  \"Message\" : \"Hello world!\",\n" +
                "  \"Timestamp\" : \"2012-05-02T00:54:06.655Z\",\n" +
                "  \"SignatureVersion\" : \"1\",\n" +
                "  \"Signature\" : \"EXAMPLEw6JRNwm1LFQL4ICB0bnXrdB8ClRMTQFGBqwLpGbM78tJ4etTwC5zU7O3tS6tGpey3ejedNdOJ+1fkIp9F2/LmNVKb5aFlYq+9rk9ZiPph5YlLmWsDcyC5T+Sy9/umic5S0UQc2PEtgdpVBahwNOdMW4JPwk0kAJJztnc=\",\n" +
                "  \"SigningCertURL\" : \"https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem\",\n" +
                "  \"UnsubscribeURL\" : \"https://sns.us-west-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-west-2:123456789012:MyTopic:c9135db0-26c4-47ec-8998-413945fb5a96\"\n" +
                "  }").byteInputStream())

        then(parse).isEqualTo(NotificationResponseDataHolder(
                "Notification",
                "22b80b92-fdea-4c2c-8f9d-bdfb0c7bf324",
                "arn:aws:sns:us-west-2:123456789012:MyTopic",
                "Hello world!",
                "2012-05-02T00:54:06.655Z",
                "1",
                "EXAMPLEw6JRNwm1LFQL4ICB0bnXrdB8ClRMTQFGBqwLpGbM78tJ4etTwC5zU7O3tS6tGpey3ejedNdOJ+1fkIp9F2/LmNVKb5aFlYq+9rk9ZiPph5YlLmWsDcyC5T+Sy9/umic5S0UQc2PEtgdpVBahwNOdMW4JPwk0kAJJztnc=",
                "https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem",
                "My First Message",
                "https://sns.us-west-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-west-2:123456789012:MyTopic:c9135db0-26c4-47ec-8998-413945fb5a96"))
    }

    @Test
    fun testParse_IncorrectTypeInJSON() {
        assertThatThrownBy {
            JsonResponseDataParser().parse(JsonResponseDataParser.SUBSCRIBE_TYPE, ("{\n" +
                    "  \"Type\" : \"UnsubscribeConfirmation\",\n" +
                    "  \"MessageId\" : \"165545c9-2a5c-472c-8df2-7ff2be2b3b1b\",\n" +
                    "  \"Token\" : \"2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                    "  \"TopicArn\" : \"arn:aws:sns:us-west-2:123456789012:MyTopic\",\n" +
                    "  \"Message\" : \"You have chosen to subscribe to the topic arn:aws:sns:us-west-2:123456789012:MyTopic.\\\\nTo confirm the subscription, visit the SubscribeURL included in this message.\",\n" +
                    "  \"SubscribeURL\" : \"https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                    "  \"Timestamp\" : \"2012-04-26T20:45:04.751Z\",\n" +
                    "  \"SignatureVersion\" : \"1\",\n" +
                    "  \"Signature\" : \"EXAMPLEpH+DcEwjAPg8O9mY8dReBSwksfg2S7WKQcikcNKWLQjwu6A4VbeS0QHVCkhRS7fUQvi2egU3N858fiTDN6bkkOxYDVrY0Ad8L10Hs3zH81mtnPk5uvvolIC1CXGu43obcgFxeL3khZl8IKvO61GWB6jI9b5+gLPoBc1Q=\",\n" +
                    "  \"SigningCertURL\" : \"https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem\"\n" +
                    "  }").byteInputStream())
        }.isInstanceOf(JsonResponseDataParser.TypeMismatchException::class.java)

        assertThatThrownBy {
            JsonResponseDataParser().parse(JsonResponseDataParser.UNSUBSCRIBE_TYPE, ("{\n" +
                    "  \"Type\" : \"SubscriptionConfirmation\",\n" +
                    "  \"MessageId\" : \"165545c9-2a5c-472c-8df2-7ff2be2b3b1b\",\n" +
                    "  \"Token\" : \"2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                    "  \"TopicArn\" : \"arn:aws:sns:us-west-2:123456789012:MyTopic\",\n" +
                    "  \"Message\" : \"You have chosen to subscribe to the topic arn:aws:sns:us-west-2:123456789012:MyTopic.\\\\nTo confirm the subscription, visit the SubscribeURL included in this message.\",\n" +
                    "  \"SubscribeURL\" : \"https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                    "  \"Timestamp\" : \"2012-04-26T20:45:04.751Z\",\n" +
                    "  \"SignatureVersion\" : \"1\",\n" +
                    "  \"Signature\" : \"EXAMPLEpH+DcEwjAPg8O9mY8dReBSwksfg2S7WKQcikcNKWLQjwu6A4VbeS0QHVCkhRS7fUQvi2egU3N858fiTDN6bkkOxYDVrY0Ad8L10Hs3zH81mtnPk5uvvolIC1CXGu43obcgFxeL3khZl8IKvO61GWB6jI9b5+gLPoBc1Q=\",\n" +
                    "  \"SigningCertURL\" : \"https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem\"\n" +
                    "  }").byteInputStream())
        }.isInstanceOf(JsonResponseDataParser.TypeMismatchException::class.java)

        assertThatThrownBy {
            JsonResponseDataParser().parse(JsonResponseDataParser.NOTIFICATION_TYPE, ("{\n" +
                    "  \"Type\" : \"SubscriptionConfirmation\",\n" +
                    "  \"MessageId\" : \"165545c9-2a5c-472c-8df2-7ff2be2b3b1b\",\n" +
                    "  \"Token\" : \"2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                    "  \"TopicArn\" : \"arn:aws:sns:us-west-2:123456789012:MyTopic\",\n" +
                    "  \"Message\" : \"You have chosen to subscribe to the topic arn:aws:sns:us-west-2:123456789012:MyTopic.\\\\nTo confirm the subscription, visit the SubscribeURL included in this message.\",\n" +
                    "  \"SubscribeURL\" : \"https://sns.us-west-2.amazonaws.com/?Action=ConfirmSubscription&TopicArn=arn:aws:sns:us-west-2:123456789012:MyTopic&Token=2336412f37fb687f5d51e6e241d09c805a5a57b30d712f794cc5f6a988666d92768dd60a747ba6f3beb71854e285d6ad02428b09ceece29417f1f02d609c582afbacc99c583a916b9981dd2728f4ae6fdb82efd087cc3b7849e05798d2d2785c03b0879594eeac82c01f235d0e717736\",\n" +
                    "  \"Timestamp\" : \"2012-04-26T20:45:04.751Z\",\n" +
                    "  \"SignatureVersion\" : \"1\",\n" +
                    "  \"Signature\" : \"EXAMPLEpH+DcEwjAPg8O9mY8dReBSwksfg2S7WKQcikcNKWLQjwu6A4VbeS0QHVCkhRS7fUQvi2egU3N858fiTDN6bkkOxYDVrY0Ad8L10Hs3zH81mtnPk5uvvolIC1CXGu43obcgFxeL3khZl8IKvO61GWB6jI9b5+gLPoBc1Q=\",\n" +
                    "  \"SigningCertURL\" : \"https://sns.us-west-2.amazonaws.com/SimpleNotificationService-f3ecfb7224c7233fe7bb5f59f96de52f.pem\"\n" +
                    "  }").byteInputStream())
        }.isInstanceOf(JsonResponseDataParser.TypeMismatchException::class.java)
    }
}
