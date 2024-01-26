

package jetbrains.buildServer.sesPlugin.data

data class MailData(
        var timestamp: String,
        var source: String,
        var sourceArn: String,
        var sendingAccountId: String,
        var messageId: String,
        var destination: List<String>,
        var headersTruncated: Boolean,
        var headers: List<HeaderData>
//        , var commonHeaders: ?
)