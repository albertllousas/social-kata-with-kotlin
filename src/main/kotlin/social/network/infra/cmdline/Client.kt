package social.network.infra.cmdline

import social.network.domain.Command
import social.network.domain.Command.PublishMessage
import social.network.domain.Command.Subscribe
import social.network.domain.Command.ViewTimeline
import social.network.domain.Command.ViewWall
import social.network.domain.CommandResponse
import social.network.domain.CommandResponse.MessagesView
import social.network.domain.CommandResponse.None
import social.network.domain.HandleCommand
import java.time.Clock


class Client(
    private val handlePublishMessageCmd: HandleCommand<PublishMessage, None>,
    private val handleSubscribeCmd: HandleCommand<Subscribe, None>,
    private val handleViewTimelineCmd: HandleCommand<ViewTimeline, MessagesView>,
    private val handleViewWallCmd: HandleCommand<ViewWall, MessagesView>,
    private val parse: (String) -> Command = ConsoleParser.parseCommand,
    private val format: (CommandResponse) -> String = ConsoleFormatter.formatCommandResponse(Clock.systemUTC())
) {

    infix fun submitCommand(command: String): String = parse(command).execute().let { format(it) }

    private fun Command.execute(): CommandResponse = when (this) {
        is PublishMessage -> handlePublishMessageCmd(this)
        is Subscribe -> handleSubscribeCmd(this)
        is ViewTimeline -> handleViewTimelineCmd(this)
        is ViewWall -> handleViewWallCmd(this)
    }
}


