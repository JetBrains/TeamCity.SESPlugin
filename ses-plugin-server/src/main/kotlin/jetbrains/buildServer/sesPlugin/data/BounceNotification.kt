package jetbrains.buildServer.sesPlugin.data

// "{\"eventType\":\"Bounce\",\"bounce\":{\"bounceType\":\"Permanent\",\"bounceSubType\":\"General\",\"bouncedRecipients\":
// [{\"emailAddress\":\"bounce@simulator.amazonses.com\",\"action\":\"failed\",\"status\":\"5.1.1\",\"diagnosticCode\":\"smtp; 550 5.1.1 user unknown\"}],
// \"timestamp\":\"2017-10-30T13:42:11.941Z\",\"feedbackId\":\"0100015f6d84aa15-81ff6d38-6a3e-4460-9808-e353c00c8736-000000\",
// \"reportingMTA\":\"dsn; a8-96.smtp-out.amazonses.com\"},
//
//
// \"mail\":{\"timestamp\":\"2017-10-30T13:42:10.918Z\"
// ,\"source\":\"notifications@teamcity-nonprod.jetbrains.com\",
// \"sourceArn\":\"arn:aws:ses:us-east-1:172801717669:identity/teamcity-nonprod.jetbrains.com\",
// \"sendingAccountId\":\"172801717669\",
// \"messageId\":\"0100015f6d84a6e6-6c7dd360-d2e7-46c8-b038-514a6f037e20-000000\",
// \"destination\":[\"bounce@simulator.amazonses.com\"],
// \"headersTruncated\":false,// \"headers\":
//
//
// [{\"name\":\"Received\",//
// \"value\":\"from unit-962 (gw.intellij.net [81.3.129.2]) by email-smtp
//    .amazonaws.com with SMTP (SimpleEmailService-2411284699) id m5EzpqQHGIcybKZokdSr for bounce@simulator.amazonses.
//    com; Mon, 30 Oct 2017 13:42:10 +0000 (UTC)\"},{\"name\":\"Date\",\"value\":\"Mon, 30 Oct 2017 16:42:08 +0300 (MSK)\"},{\"name\":\"From\",\"value\":\"notifications@teamcity-nonprod.jetbrains.com\"},{\"name\":\"To\",\"value\":\"bounce@simulator.amazonses.com\"},{\"name\":\"Message-ID\",\"value\":\"<1189489180.16.1509370930486.JavaMail.linfar@unit-962>\"},{\"name\":\"Subject\",\"value\":\"[TeamCity, SUCCESSFUL] Build project :: notify bounce #5\"},{\"name\":\"MIME-Version\",\"value\":\"1.0\"},{\"name\":\"Content-Type\",\"value\":\"multipart/alternative;  boundary=\\\"----=_Part_15_920035328.1509370928122\\\"\"},{\"name\":\"X-SES-CONFIGURATION-SET\",\"value\":\"teamcity-ses-nonprod\"}],\"commonHeaders\":{\"from\":[\"notifications@teamcity-nonprod.jetbrains.com\"],\"date\":\"Mon, 30 Oct 2017 16:42:08 +0300 (MSK)\",\"to\":[\"bounce@simulator.amazonses.com\"],\"messageId\":\"0100015f6d84a6e6-6c7dd360-d2e7-46c8-b038-514a6f037e20-000000\",\"subject\":\"[TeamCity, SUCCESSFUL] Build project :: notify bounce #5\"},\"tags\":{\"ses:configuration-set\":[\"teamcity-ses-nonprod\"],\"ses:source-ip\":[\"81.3.129.2\"],\"ses:from-domain\":[\"teamcity-nonprod.jetbrains.com\"],\"ses:caller-identity\":[\"teamcity-ses-nonprod-ses-user\"]}}}\n",
data class BounceNotification(
        override var eventType: String,
        override var bounce: BounceData,
        override var mail: MailData
) : SESBounceNotification {
    override fun getBounceType() = bounce.bounceType

    override fun getBounceSubType() = bounce.bounceSubType

    override fun getRecipients() = bounce.bouncedRecipients
}