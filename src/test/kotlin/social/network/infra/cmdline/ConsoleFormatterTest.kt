package social.network.infra.cmdline

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import social.network.domain.CommandResponse
import social.network.domain.Message
import social.network.domain.UserName
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ConsoleFormatterTest {

    private val clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val format = ConsoleFormatter.formatCommandResponse(clock)

    @Test
    fun `should format a none response of a command`() {
        format(CommandResponse.None) shouldBe ""
    }

    @Test
    fun `should format a messages view response of a command`() {
        val messages = listOf(
            Message(UserName("Charlie"), "I'm in New York today!", LocalDateTime.parse("2007-12-03T10:15:15.00")),
            Message(UserName("Bob"), "Good game though.", LocalDateTime.parse("2007-12-03T10:14:15.00")),
            Message(UserName("Bob"), "Damn! We lost!", LocalDateTime.parse("2007-12-03T10:13:15.00")),
        )

        val result = format(CommandResponse.MessagesView(messages))

        result shouldBe """
        Charlie - I'm in New York today! (15 seconds ago)
        Bob - Good game though. (1 minute ago)
        Bob - Damn! We lost! (2 minutes ago)
        """.trimIndent()
    }
}
