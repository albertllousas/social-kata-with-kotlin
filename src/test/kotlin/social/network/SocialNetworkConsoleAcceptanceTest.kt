package social.network

import com.mercateo.test.clock.TestClock
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import social.network.domain.*
import social.network.infra.cmdline.Client
import social.network.infra.cmdline.ConsoleFormatter
import social.network.infra.repository.InMemoryMessagesRepository
import social.network.infra.repository.InMemoryUsersRepository
import java.time.Duration
import java.time.OffsetDateTime

class SocialNetworkConsoleAcceptanceTest {

    private val messagesRepository = InMemoryMessagesRepository()

    private val userRepository = InMemoryUsersRepository(
        mutableMapOf(
            UserName("Alice") to User(UserName("Alice"), following = emptyList()),
            UserName("Bob") to User(UserName("Bob"), following = emptyList()),
            UserName("Charlie") to User(UserName("Charlie"), following = emptyList())
        )
    )

    private val seed = OffsetDateTime.parse("2007-12-03T10:15:30.00Z")

    private val clock = TestClock.fixed(seed)

    private val console = Client(
        handlePublishMessageCmd = handlePublishMessage(messagesRepository, clock),
        handleSubscribeCmd = handleSubscribe(userRepository),
        handleViewTimelineCmd = handleViewTimeline(messagesRepository),
        handleViewWallCmd = handleViewWall(userRepository, messagesRepository),
        format = ConsoleFormatter.formatCommandResponse(clock)
    )

    @Test
    fun `a user can view other user's timeline`() {
        console submitCommand "Bob -> Damn! We lost!"
        clock.fastForward(Duration.ofMinutes(1))
        console submitCommand "Bob -> Good game though."
        clock.fastForward(Duration.ofMinutes(1))

        val output = console submitCommand "Bob"

        output shouldBe """
            Bob - Good game though. (1 minute ago)
            Bob - Damn! We lost! (2 minutes ago)
        """.trimIndent()
    }

    @Test
    fun `a user's wall should show their timeline and an aggregated list of all subscriptions`() {
        console submitCommand "Alice -> I love the weather today"
        clock.fastForward(Duration.ofMinutes(3))
        console submitCommand "Bob -> Damn! We lost!"
        clock.fastForward(Duration.ofMinutes(1))
        console submitCommand "Bob -> Good game though."
        clock.fastForward(Duration.ofMinutes(1))
        console submitCommand "Charlie -> I'm in New York today! Anyone wants to have a coffee?"
        clock.fastForward(Duration.ofSeconds(15))
        console submitCommand "Charlie follows Bob"
        console submitCommand "Charlie follows Alice"

        val output = console submitCommand "Charlie wall"

        output shouldBe """
            Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)
            Bob - Good game though. (1 minute ago)
            Bob - Damn! We lost! (2 minutes ago)
            Alice - I love the weather today (5 minutes ago)
        """.trimIndent()
    }
}

