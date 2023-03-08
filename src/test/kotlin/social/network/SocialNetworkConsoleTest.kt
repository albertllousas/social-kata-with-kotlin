package social.network

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class SocialNetworkConsoleTest {

    private val timelines = mockk<PersonalTimelines>()

    private val users = mockk<Users>()

    private val clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val console = SocialNetworkConsole(users, timelines, clock)

    @Nested
    inner class SubmitCommandTest {

        @Test
        fun `should post a message to a personal timeline`() {
            every {
                timelines.publish(
                    PublishMessage(
                        user = User("Alice"),
                        message = Message("I love the weather today", LocalDateTime.parse("2007-12-03T10:15:30"))
                    )
                )
            } just Runs

            val result = console.submitCommand("Alice -> I love the weather today")

            assertThat(result).isEqualTo("")
        }
    }

    @Nested
    inner class ViewTimelineCommandTest {

        @Test
        fun `should read the timeline from a user`() {
            val firstMessage = Message("Good game though.", LocalDateTime.parse("2007-12-03T10:14:30"))
            val secondMessage = Message("Damn! We lost!", LocalDateTime.parse("2007-12-03T10:13:30"))
            every { timelines.view(ViewTimeline(from = User("Alice"))) } returns listOf(firstMessage, secondMessage)

            val result = console.submitCommand("Alice")

            assertThat(result).isEqualTo("Good game though. (1 minute ago)\nDamn! We lost! (2 minutes ago)")
        }

        @Test
        fun `should read an empty timeline from a user without any message`() {
            every { timelines.view(ViewTimeline(from = User("Alice"))) } returns emptyList()

            val result = console.submitCommand("Alice")

            assertThat(result).isEqualTo("")
        }

    }

    @Nested
    inner class FollowingCommandTest {

        @Test
        fun `should follow an existent user`() {
            every {
                users.subscribe(SubscribeCommand(follower = User("Charlie"), followed = User("Alice")))
            } just Runs

            val result = console.submitCommand("Charlie follows Alice")

            assertThat(result).isEqualTo("")
        }
    }

    @Nested
    inner class WallCommandTest {

        @Test
        fun `should print the wall of a user`() {
            val fstBobMsg = Message("Good game though.", LocalDateTime.parse("2007-12-03T10:14:30"))
            val sndBobMsg = Message("Damn! We lost!", LocalDateTime.parse("2007-12-03T10:13:30"))
            val charlieMsg = Message("I'm in New York today! Anyone wants to have a coffee?", LocalDateTime.parse("2007-12-03T10:15:15"))
            every { users.findFollowedUsers(User("Charlie")) } returns listOf(User("Bob"))
            every { timelines.view(ViewTimeline(from = User("Bob"))) } returns listOf(fstBobMsg, sndBobMsg)
            every { timelines.view(ViewTimeline(from = User("Charlie"))) } returns listOf(charlieMsg)

            val result = console.submitCommand("Charlie wall")

            assertThat(result).isEqualTo("""
                Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)\n
                Bob - Good game though. (1 minute ago)\n
                Bob - Damn! We lost! (2 minutes ago)
                """)
        }
    }
}
