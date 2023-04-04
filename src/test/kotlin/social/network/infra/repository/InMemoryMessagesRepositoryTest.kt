package social.network.infra.repository

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import social.network.domain.Message
import social.network.domain.UserName
import java.time.LocalDateTime.parse

class InMemoryMessagesRepositoryTest {

    private val msgs = mutableListOf<Message>()

    private val repository = InMemoryMessagesRepository(msgs)

    @Test
    fun `should save a message`() {
        val msg = Message(UserName("Bob"), "Damn! We lost!", parse("2007-12-03T10:13:15.00"))

        repository.save(msg)

        msgs shouldBe listOf(msg)
    }

    @Test
    fun `should find all messages by username`() {
        Message(UserName("John"), "Damn! We lost!", parse("2007-12-03T10:13:15.00")).also(repository::save)
        val janesMsg = Message(UserName("Jane"), "Hi", parse("2007-12-03T10:12:15.00")).also(repository::save)

        repository.findAll(UserName("Jane")) shouldBe listOf(janesMsg)
    }
}
