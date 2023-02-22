package social.network

import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.MINUTES

class SocialNetworkConsole(
    private val timelines: PersonalTimelines,
    private val clock: Clock
) {

    companion object {
        const val PUBLISH_DELIMITER = " -> "
    }

    fun submitCommand(command: String): String =
        when {
            command.contains(PUBLISH_DELIMITER) -> post(command)
            else -> view(command)
        }

    private fun post(command: String): String = command
        .split(PUBLISH_DELIMITER)
        .also { timelines.publish(User(it[0]), Message(it[1], LocalDateTime.now(clock))) }
        .let { "" }

    private fun view(command: String): String = timelines
        .view(User(command))
        .joinToString("\n") { it.asFormatted(clock) }

}

private fun calculateMinutesTillNowFor(messageTs: LocalDateTime, clock: Clock): String {
    val minutes = MINUTES.between(messageTs, LocalDateTime.now(clock))
    return if (minutes <= 1) {
        "$minutes minute"
    } else {
        "$minutes minutes"
    }
}

fun Message.asFormatted(clock: Clock): String = "${this.value} (${calculateMinutesTillNowFor(this.ts, clock)} ago)"

