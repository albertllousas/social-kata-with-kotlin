package social.network

import java.time.Clock

class SocialNetworkConsole(
    private val timelines: PersonalTimelines,
    clock: Clock
) {

    companion object {
        const val PUBLISH_DELIMITER = " -> "
    }

    fun submitCommand(command: String): String =
        when {
            command.contains(PUBLISH_DELIMITER) -> post(command)
            else -> view(command)
        }

    private fun post(command: String) = command
        .split(PUBLISH_DELIMITER)
        .also { timelines.publish(User(it[0]), Message(it[1])) }
        .let { ""}

    private fun view(command: String) = timelines.view(User(command)).joinToString("\n") { it.value }

}
