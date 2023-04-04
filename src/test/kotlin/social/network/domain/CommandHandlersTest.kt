package social.network.domain

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CommandHandlersTest {

    private val clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val messagesRepository = mockk<MessagesRepository>(relaxed = true)

    private val usersRepository = mockk<UsersRepository>(relaxed = true)

    private val handlePublishMessageCmd = handlePublishMessage(messagesRepository, clock)

    private val handleSubscribeCmd = handleSubscribe(usersRepository)

    private val handleViewTimelineCmd = handleViewTimeline(messagesRepository)

    private val handleViewWallCmd = handleViewWall(usersRepository, messagesRepository)

    private val someMsgs = listOf(
        Message(UserName("Bob"), "Damn! We lost!", LocalDateTime.parse("2007-12-03T10:13:15.00")),
        Message(UserName("Bob"), "Good game though.", LocalDateTime.parse("2007-12-03T10:14:15.00"))
    )

    @Test
    fun `should handle a 'PublishMessage' command`() {
        val cmd = Command.PublishMessage("Jane", "Hello there!")

        val result = handlePublishMessageCmd(cmd)

        result shouldBe CommandResponse.None
        verify { messagesRepository.save(Message(UserName("Jane"), "Hello there!", LocalDateTime.now(clock))) }
    }

    @Test
    fun `should handle a 'Subscribe' command`() {
        val cmd = Command.Subscribe(follower = "Jane", followed = "John")
        val jane = User(UserName("Jane"), emptyList())
        every { usersRepository.find(UserName("Jane")) } returns jane

        val result = handleSubscribeCmd(cmd)

        result shouldBe CommandResponse.None
        verify { usersRepository.save(jane.copy(following = listOf(UserName("John")))) }
    }

    @Test
    fun `should handle a 'Subscribe' command when the follower does not exists`() {
        val cmd = Command.Subscribe(follower = "Jane", followed = "John")
        every { usersRepository.find(UserName("Jane")) } returns null

        val result = handleSubscribeCmd(cmd)

        result shouldBe CommandResponse.None
    }

    @Test
    fun `should handle a 'ViewTimeline' command`() {
        val cmd = Command.ViewTimeline("Jane")
        every { messagesRepository.findAll(UserName("Jane")) } returns someMsgs

        val result = handleViewTimelineCmd(cmd)

        result.messages shouldContainAll someMsgs
    }

    @Test
    fun `should handle a 'ViewWall' command`() {
        val jane = User(UserName("Jane"), following = listOf(UserName("John")))
        val cmd = Command.ViewWall("Jane")
        val johnMsgs = listOf(Message(UserName("John"), "Hi!", LocalDateTime.parse("2007-12-03T10:13:15.00")))
        every { usersRepository.find(UserName("Jane")) } returns jane
        every { messagesRepository.findAll(UserName("Jane")) } returns someMsgs
        every { messagesRepository.findAll(UserName("John")) } returns johnMsgs

        val result = handleViewWallCmd(cmd)

        result.messages shouldContainAll  someMsgs + johnMsgs
    }
}
