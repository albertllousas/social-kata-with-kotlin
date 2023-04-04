package social.network

import social.network.domain.User
import social.network.domain.UserName
import social.network.domain.handlePublishMessage
import social.network.domain.handleSubscribe
import social.network.domain.handleViewTimeline
import social.network.domain.handleViewWall
import social.network.infra.cmdline.Client
import social.network.infra.repository.InMemoryMessagesRepository
import social.network.infra.repository.InMemoryUsersRepository
import java.time.Clock

fun buildClient(): Client {
    val messagesRepository = InMemoryMessagesRepository()
    val userRepository = InMemoryUsersRepository(
        mutableMapOf(
            UserName("Alice") to User(UserName("Alice"), following = emptyList()),
            UserName("Bob") to User(UserName("Bob"), following = emptyList()),
            UserName("Charlie") to User(UserName("Charlie"), following = emptyList())
        )
    )
    return Client(
        handlePublishMessageCmd = handlePublishMessage(messagesRepository, Clock.systemUTC()),
        handleSubscribeCmd = handleSubscribe(userRepository),
        handleViewTimelineCmd = handleViewTimeline(messagesRepository),
        handleViewWallCmd = handleViewWall(userRepository, messagesRepository),
    )
}

fun main() {
    val quitCmd = "q!!"
    val client = buildClient()
    println("'$quitCmd' to exit")
    while (true) {
        val input = readln()
        if (input == quitCmd) break else println(client submitCommand input)
    }
}