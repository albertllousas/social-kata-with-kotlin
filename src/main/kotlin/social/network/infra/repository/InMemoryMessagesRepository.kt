package social.network.infra.repository

import social.network.domain.Message
import social.network.domain.MessagesRepository
import social.network.domain.UserName

class InMemoryMessagesRepository(private val messages: MutableList<Message> = mutableListOf()) : MessagesRepository {
    override fun save(message: Message) {
        messages.remove(message).also { messages.add(message) }
    }

    override fun findAll(user: UserName): List<Message> = messages.filter { it.userName == user }
}