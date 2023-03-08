package social.network

import java.time.Clock
import java.time.LocalDateTime.*

class SocialNetworkConsole(
    private val users: Users,
    private val timelines: PersonalTimelines,
    private val clock: Clock,
) {

    companion object {
        const val PUBLISH_DELIMITER = " -> "
        const val FOLLOW_DELIMITER = " follows "
        const val WALL_SUFFIX = " wall"
    }

    fun submitCommand(command: String): String = parse(command).let(::execute)

    private fun parse(command: String): Command =
        when {
            command.contains(PUBLISH_DELIMITER) ->
                command.split(PUBLISH_DELIMITER).let { PublishMessage(User(it[0]), Message(it[1], now(clock))) }
            command.contains(FOLLOW_DELIMITER) ->
                command.split(FOLLOW_DELIMITER).let { SubscribeCommand(User(it[0]), User(it[1])) }
            command.endsWith(WALL_SUFFIX) ->
                GetWallCommand(User(command.removeSuffix(WALL_SUFFIX)))
            else -> ViewTimeline(User(command))
        }

    private fun execute(command: Command): String = when (command) {
        is PublishMessage ->
            timelines.publish(command).let { "" }
        is ViewTimeline ->
            timelines.view(command).joinToString("\n") { it.asFormatted(clock) }
        is SubscribeCommand -> users.subscribe(command).let { "" }
        is GetWallCommand -> getWallCommand(command).joinToString("\n") { it.asFormatted(clock) }
    }

    private fun getWallCommand(command: GetWallCommand): List<Message> = TODO()
}


private fun getMinutesFormat(minutes: Long): String =
    when (minutes) {
        in 0..1 -> "$minutes minute"
        else -> "$minutes minutes"
    }

fun Message.asFormatted(clock: Clock): String = "${this.value} (${getMinutesFormat(this.getMinutesTillNow(clock))} ago)"

