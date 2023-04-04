package social.network.infra.cmdline

import social.network.domain.Command
import social.network.domain.Command.PublishMessage
import social.network.domain.Command.Subscribe
import social.network.domain.Command.ViewTimeline
import social.network.domain.Command.ViewWall

const val PUBLISH_DELIMITER = " -> "
const val FOLLOW_DELIMITER = " follows "
const val WALL_SUFFIX = " wall"

object ConsoleParser {

    val parseCommand: (String) -> Command = { cmd ->
        when {
            cmd.contains(PUBLISH_DELIMITER) -> cmd.split(PUBLISH_DELIMITER).let { PublishMessage(it[0], it[1]) }
            cmd.contains(FOLLOW_DELIMITER) -> cmd.split(FOLLOW_DELIMITER).let { Subscribe(it[0], it[1]) }
            cmd.endsWith(WALL_SUFFIX) -> ViewWall(cmd.removeSuffix(WALL_SUFFIX))
            else -> ViewTimeline(cmd)
        }
    }
}
