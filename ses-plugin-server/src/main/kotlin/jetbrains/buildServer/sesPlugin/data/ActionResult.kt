package jetbrains.buildServer.sesPlugin.data

import jetbrains.buildServer.sesPlugin.teamcity.util.JsonModelComponent

data class ActionResult(val status: Boolean = true, val error: String = "") : JsonModelComponent