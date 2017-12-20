package jetbrains.buildServer.server.responsible

import jetbrains.buildServer.serverSide.SBuildType
import jetbrains.buildServer.serverSide.SProject
import java.util.*

interface ResponsibleContactProvider {
    fun get(project: SProject): Optional<String>
    fun get(buildType: SBuildType): Optional<String>
    fun getGlobal(): Optional<String>
}

