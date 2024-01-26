

package jetbrains.buildServer.sesPlugin.teamcity

class SESIntegrationConfigImpl : SESIntegrationConfig {
    override fun isDisableVerifiedMailOnBounce(): Boolean = false
    override fun isDisableSendingMailOnBounce(): Boolean = true
}