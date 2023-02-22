package social.network

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class SocialNetworkConsoleTest {

    private val timelines = mockk<PersonalTimelines>(relaxed = true)

    private val clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val console = SocialNetworkConsole(timelines, clock)

    @Test
    fun `should post a message to a personal timeline`() {
        val result = console.submitCommand("Alice -> I love the weather today")

        assertThat(result).isEqualTo("")
        verify {
            timelines.publish(User("Alice"), Message("I love the weather today", LocalDateTime.parse("2007-12-03T10:15:30")))
        }
    }

    @Test
    fun `should read the timeline from a user`() {
        val firstMessage = Message("Good game though.", LocalDateTime.parse("2007-12-03T10:14:30"))
        val secondMessage = Message("Damn! We lost!", LocalDateTime.parse("2007-12-03T10:13:30"))
        every { timelines.view(User("Alice")) } returns listOf(firstMessage, secondMessage)

        val result = console.submitCommand("Alice")

        assertThat(result).isEqualTo("Good game though. (1 minute ago)\nDamn! We lost! (2 minutes ago)")
    }
}
