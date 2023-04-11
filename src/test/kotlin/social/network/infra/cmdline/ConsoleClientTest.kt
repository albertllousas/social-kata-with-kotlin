package social.network.infra.cmdline

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import social.network.domain.Command
import social.network.domain.Command.PublishMessage
import social.network.domain.Command.Subscribe
import social.network.domain.Command.ViewTimeline
import social.network.domain.Command.ViewWall
import social.network.domain.CommandResponse
import social.network.domain.CommandResponse.MessagesView
import social.network.domain.CommandResponse.None
import social.network.domain.HandleCommand

class ConsoleClientTest {

    private val handlePublishMessageCmd = mockk<HandleCommand<PublishMessage, None>>()
    private val handleSubscribeCmd = mockk<HandleCommand<Subscribe, None>>()
    private val handleViewTimelineCmd = mockk<HandleCommand<ViewTimeline, MessagesView>>()
    private val handleViewWallCmd = mockk<HandleCommand<ViewWall, MessagesView>>()
    private val parse = mockk<(String) -> Command>()
    private val format = mockk<(CommandResponse) -> String>()

    private val consoleClient = ConsoleClient(
        handlePublishMessageCmd, handleSubscribeCmd, handleViewTimelineCmd, handleViewWallCmd, parse, format
    )

    @Test
    fun `should submit a publish message command from command line`() {
        every { parse("Alice -> I love the weather today") } returns PublishMessage("Alice", "I love the weather today")
        every {
            hint(None::class)
            handlePublishMessageCmd(PublishMessage("Alice", "I love the weather today"))
        } returns None
        every { format(None) } returns ""

        val result = consoleClient submitCommand "Alice -> I love the weather today"

        result shouldBe ""
    }

    @Test
    fun `should submit a subscribe command from command line`() {
        every { parse("Charlie follows Alice") } returns Subscribe(follower = "Charlie", followed = "Alice")
        every {
            hint(None::class)
            handleSubscribeCmd(Subscribe(follower = "Charlie", followed = "Alice"))
        } returns None
        every { format(None) } returns ""

        val result = consoleClient submitCommand "Charlie follows Alice"

        result shouldBe ""
    }

    @Test
    fun `should submit a view timeline command from command line`() {
        val someMessages = MessagesView(emptyList())
        every { parse("Alice") } returns ViewTimeline(from = "Alice")
        every {
            hint(MessagesView::class)
            handleViewTimelineCmd(ViewTimeline(from = "Alice"))
        } returns someMessages
        every { format(someMessages) } returns "Alice timeline"

        val result = consoleClient submitCommand "Alice"

        result shouldBe "Alice timeline"
    }

    @Test
    fun `should submit a view wall command from command line`() {
        val someMessages = MessagesView(emptyList())
        every { parse("Alice wall") } returns ViewWall(userName = "Alice")
        every {
            hint(MessagesView::class)
            handleViewWallCmd(ViewWall("Alice"))
        } returns someMessages
        every { format(someMessages) } returns "Alice wall content"

        val result = consoleClient submitCommand "Alice wall"

        result shouldBe "Alice wall content"
    }

}