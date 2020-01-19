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

/*
{
  "eventType": "Complaint",
  "complaint": {
    "complainedRecipients": [
      {
        "emailAddress": "complaint@simulator.amazonses.com"
      }
    ],
    "timestamp": "2017-12-26T14:30:00.326Z",
    "feedbackId": "01000160933aeb00-f04dc65d-1f9d-4121-b1f6-b62a40ce8fa8-000000",
    "userAgent": "Amazon SES Mailbox Simulator",
    "complaintFeedbackType": "abuse",
    "arrivalDate": "2017-12-26T14:30:00.326Z"
  },
  "mail": {
    "timestamp": "2017-12-26T14:29:59.671Z",
    "source": "notifications@teamcity-nonprod.jetbrains.com",
    "sourceArn": "arn:aws:ses:us-east-1:172801717669:identity/teamcity-nonprod.jetbrains.com",
    "sendingAccountId": "172801717669",
    "messageId": "01000160933ae8f7-c166b8c4-b5ac-4d60-b183-d18a29289e08-000000",
    "destination": [
      "complaint@simulator.amazonses.com"
    ],
    "headersTruncated": false,
    "headers": [
      {
        "name": "Received",
        "value": "from UNIT-431.Labs.IntelliJ.Net (gw2.intellij.net [80.76.244.114]) by email-smtp.amazonaws.com with SMTP (SimpleEmailService-2672117487) id i8OzKZhf4fPkX2lmVSbB for complaint@simulator.amazonses.com; Tue, 26 Dec 2017 14:29:59 +0000 (UTC)"
      },
      {
        "name": "Date",
        "value": "Tue, 26 Dec 2017 17:29:56 +0300 (MSK)"
      },
      {
        "name": "From",
        "value": "notifications@teamcity-nonprod.jetbrains.com"
      },
      {
        "name": "To",
        "value": "complaint@simulator.amazonses.com"
      },
      {
        "name": "Message-ID",
        "value": "<915221666.4.1514298598904.JavaMail.Inna.Yankelevich@UNIT-431>"
      },
      {
        "name": "Subject",
        "value": "[TeamCity, SUCCESSFUL] Build Project with dependency :: BuildConfigB #7"
      },
      {
        "name": "MIME-Version",
        "value": "1.0"
      },
      {
        "name": "Content-Type",
        "value": "multipart/alternative;  boundary=
        \
        "---- = _Part_3_1393798475.1514298596075\""
      },
      {
        "name": "X-SES-CONFIGURATION-SET",
        "value": "teamcity-ses-nonprod"
      }
    ],
    "commonHeaders": {
      "from": [
        "notifications@teamcity-nonprod.jetbrains.com"
      ],
      "date": "Tue, 26 Dec 2017 17:29:56 +0300 (MSK)",
      "to": [
        "complaint@simulator.amazonses.com"
      ],
      "messageId": "01000160933ae8f7-c166b8c4-b5ac-4d60-b183-d18a29289e08-000000",
      "subject": "[TeamCity, SUCCESSFUL] Build Project with dependency :: BuildConfigB #7"
    },
    "tags": {
      "ses:configuration-set": [
        "teamcity-ses-nonprod"
      ],
      "ses:source-ip": [
        "80.76.244.114"
      ],
      "ses:from-domain": [
        "teamcity-nonprod.jetbrains.com"
      ],
      "ses:caller-identity": [
        "teamcity-ses-nonprod-ses-user"
      ]
    }
  }
}
*/

/*
  "complaint": {
    "complainedRecipients": [
      {
        "emailAddress": "complaint@simulator.amazonses.com"
      }
    ],
    "timestamp": "2017-12-26T14:30:00.326Z",
    "feedbackId": "01000160933aeb00-f04dc65d-1f9d-4121-b1f6-b62a40ce8fa8-000000",
    "userAgent": "Amazon SES Mailbox Simulator",
    "complaintFeedbackType": "abuse",
    "arrivalDate": "2017-12-26T14:30:00.326Z"
  },

 */

data class ComplaintData(
        var complainedRecipients: List<Recipient>,
        var timestamp: String,
        var feedbackId: String,
        var userAgent: String,
        var complaintFeedbackType: String,
        var arrivalDate: String
)