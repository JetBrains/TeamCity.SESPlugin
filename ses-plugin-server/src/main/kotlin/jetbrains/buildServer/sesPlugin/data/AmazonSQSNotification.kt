/*
 * Copyright 2000-2020 JetBrains s.r.o.
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

package jetbrains.buildServer.sesPlugin.data

data class AmazonSQSNotification(
        var Type: String,
        var MessageId: String,
        var TopicArn: String,
        var Subject: String,
        var Message: String,
        var Timestamp: String,
        var SignatureVersion: String,
        var Signature: String,
        var SigningCertURL: String,
        var UnsubscribeURL: String?
)

/**
Eg:
({
"Type" : "Notification",
"MessageId" : "ace76e28-4a8d-5b11-a516-3c5964f9d54d",
"TopicArn" : "arn:aws:sns:us-east-1:172801717669:teamcity-ses-nonprod-bounces",
"Subject" : "Amazon SES Email Event Notification",
"Message" : "{\"eventType\":\"Bounce\",\"bounce\":{\"bounceType\":\"Permanent\",\"bounceSubType\":\"General\",\"bouncedRecipients\":[{\"emailAddress\":\"bounce@simulator.amazonses.com\",\"action\":\"failed\",\"status\":\"5.1.1\",\"diagnosticCode\":\"smtp; 550 5.1.1 user unknown\"}],\"timestamp\":\"2017-10-30T13:42:11.941Z\",\"feedbackId\":\"0100015f6d84aa15-81ff6d38-6a3e-4460-9808-e353c00c8736-000000\",\"reportingMTA\":\"dsn; a8-96.smtp-out.amazonses.com\"},\"mail\":{\"timestamp\":\"2017-10-30T13:42:10.918Z\",\"source\":\"notifications@teamcity-nonprod.jetbrains.com\",\"sourceArn\":\"arn:aws:ses:us-east-1:172801717669:identity/teamcity-nonprod.jetbrains.com\",\"sendingAccountId\":\"172801717669\",\"messageId\":\"0100015f6d84a6e6-6c7dd360-d2e7-46c8-b038-514a6f037e20-000000\",\"destination\":[\"bounce@simulator.amazonses.com\"],\"headersTruncated\":false,\"headers\":[{\"name\":\"Received\",\"value\":\"from unit-962 (gw.intellij.net [81.3.129.2]) by email-smtp.amazonaws.com with SMTP (SimpleEmailService-2411284699) id m5EzpqQHGIcybKZokdSr for bounce@simulator.amazonses.com; Mon, 30 Oct 2017 13:42:10 +0000 (UTC)\"},{\"name\":\"Date\",\"value\":\"Mon, 30 Oct 2017 16:42:08 +0300 (MSK)\"},{\"name\":\"From\",\"value\":\"notifications@teamcity-nonprod.jetbrains.com\"},{\"name\":\"To\",\"value\":\"bounce@simulator.amazonses.com\"},{\"name\":\"Message-ID\",\"value\":\"<1189489180.16.1509370930486.JavaMail.linfar@unit-962>\"},{\"name\":\"Subject\",\"value\":\"[TeamCity, SUCCESSFUL] Build project :: notify bounce #5\"},{\"name\":\"MIME-Version\",\"value\":\"1.0\"},{\"name\":\"Content-Type\",\"value\":\"multipart/alternative;  boundary=\\\"----=_Part_15_920035328.1509370928122\\\"\"},{\"name\":\"X-SES-CONFIGURATION-SET\",\"value\":\"teamcity-ses-nonprod\"}],\"commonHeaders\":{\"from\":[\"notifications@teamcity-nonprod.jetbrains.com\"],\"date\":\"Mon, 30 Oct 2017 16:42:08 +0300 (MSK)\",\"to\":[\"bounce@simulator.amazonses.com\"],\"messageId\":\"0100015f6d84a6e6-6c7dd360-d2e7-46c8-b038-514a6f037e20-000000\",\"subject\":\"[TeamCity, SUCCESSFUL] Build project :: notify bounce #5\"},\"tags\":{\"ses:configuration-set\":[\"teamcity-ses-nonprod\"],\"ses:source-ip\":[\"81.3.129.2\"],\"ses:from-domain\":[\"teamcity-nonprod.jetbrains.com\"],\"ses:caller-identity\":[\"teamcity-ses-nonprod-ses-user\"]}}}\n",
"Timestamp" : "2017-10-30T13:42:12.183Z",
"SignatureVersion" : "1",
"Signature" : "B2YbtxYSMAw+ui5y1iGGZ56mbAFQ7J/WIofZ415NVM2knUk2V4TFM9Ri/OqY3trv9a9+86SqtE/3K7UT8iVYyLhkFre/rzpwfoi+mxwY6k/4YDerYjpbMMGoaY8VnW1iWo4G3494DUS3IUjAe5Cmm6ZS3OwNeGE1GYxMVtD51Ai3Qe5mVvZ0oOb1OMgfnO/uM9ylo7jW8sa5592qbGJg+W8TQYY3EsKgJiwLZ5DaHMIxqkJoDjdIRzmS3oaOUEGaQhN8T2hrOHk5/Un6Ce5TdFQPvitpJpEIR3xWMPNKregWZyFR7a3foEXFUOZ8j0cloIYrzSUAoflxGNKvitQ8vA==",
"SigningCertURL" : "https://sns.us-east-1.amazonaws.com/SimpleNotificationService-433026a4050d206028891664da859041.pem",
"UnsubscribeURL" : "https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:172801717669:teamcity-ses-nonprod-bounces:ce4476d2-ec34-4167-8a52-941750ef1cc6"
})
 **/