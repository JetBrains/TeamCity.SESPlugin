

package jetbrains.buildServer.sesPlugin.teamcity

interface SESIntegrationConfig {
    fun isDisableVerifiedMailOnBounce(): Boolean
    fun isDisableSendingMailOnBounce(): Boolean

}