package social.network.domain

import java.time.LocalDateTime

interface MessagesRepository {

    fun save(message: Message)
    fun findAll(user: UserName): List<Message>
}

data class Message(val userName: UserName, val value: String, val ts: LocalDateTime)
