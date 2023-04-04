package social.network.domain

import social.network.domain.Command.PublishMessage
import social.network.domain.Command.Subscribe
import social.network.domain.Command.ViewTimeline
import social.network.domain.Command.ViewWall
import social.network.domain.CommandResponse.MessagesView
import social.network.domain.CommandResponse.None
import java.time.Clock
import java.time.LocalDateTime.now

sealed interface Command {
    data class PublishMessage(val userName: String, val message: String) : Command
    data class ViewTimeline(val from: String) : Command
    data class Subscribe(val follower: String, val followed: String) : Command
    data class ViewWall(val userName: String) : Command
}

sealed interface CommandResponse {

    object None : CommandResponse
    data class MessagesView(val messages: List<Message>) : CommandResponse
}

fun interface HandleCommand<C : Command, R : CommandResponse> : (C) -> R

val handlePublishMessage = { messages: MessagesRepository, clock: Clock ->
    HandleCommand { cmd: PublishMessage ->
        messages.save(Message(UserName(cmd.userName), cmd.message, now(clock))).let { None }
    }
}

val handleViewTimeline = { messages: MessagesRepository ->
    HandleCommand { cmd: ViewTimeline ->
        messages.findAll(UserName(cmd.from)).let(::TimeLine).sortByRecentFirst().let(::MessagesView)
    }
}

val handleSubscribe = { users: UsersRepository ->
    HandleCommand { cmd: Subscribe ->
        users.find(UserName(cmd.follower))?.follows(UserName(cmd.followed))?.also(users::save).let { None }
    }
}

val handleViewWall = { users: UsersRepository, messages: MessagesRepository ->
    HandleCommand { cmd: ViewWall ->
        val userNames = users.find(UserName(cmd.userName))?.let { it.following + it.userName } ?: emptyList()
        userNames
            .let { it.map { userName -> messages.findAll(userName) } }
            .flatten()
            .let(::TimeLine)
            .sortByRecentFirst()
            .let(::MessagesView)
    }
}
