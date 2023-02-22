package social.network

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SocialNetworkConsoleTest {

    private val timelines = mockk<PersonalTimelines>(relaxed = true)

    private val console = SocialNetworkConsole(timelines)

    @Test
    fun `should post a message to a personal timeline`() {
        val result = console.submitCommand("Alice -> I love the weather today")

        assertThat(result).isEqualTo(Unit)
        verify {
            timelines.publish(User("Alice"), Message("I love the weather today"))
        }
    }

    @Test
    fun `should read the timeline from a user when only one message`() {
        val message = Message("I love the weather today")
        every { timelines.viewByUser("Alice") } returns listOf(message)

        val result = console.submitCommand("Alice")

        assertThat(result).isEqualTo(message.value)
    }

}