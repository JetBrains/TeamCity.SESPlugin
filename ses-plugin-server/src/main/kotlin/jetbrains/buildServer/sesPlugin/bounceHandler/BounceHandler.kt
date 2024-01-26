

package jetbrains.buildServer.sesPlugin.bounceHandler

/**
 * Interface handling bounce events (hard ones only)
 */
interface BounceHandler {
    fun handleBounces(mails: Sequence<String>)
}