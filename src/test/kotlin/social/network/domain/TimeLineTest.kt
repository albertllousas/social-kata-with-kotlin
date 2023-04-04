package social.network.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TimeLineTest {

    @Test
    fun `should sort messages by recent first`() {
        val fstBobMsg = Message(UserName("Bob"), "Damn! We lost!", LocalDateTime.parse("2007-12-03T10:13:15.00"))
        val sndBobMsg = Message(UserName("Bob"), "Good game though.", LocalDateTime.parse("2007-12-03T10:14:15.00"))
        val charliesMsg =
            Message(UserName("Charlie"), "I'm in New York today!", LocalDateTime.parse("2007-12-03T10:15:15.00"))
        val messages = listOf(fstBobMsg, sndBobMsg, charliesMsg)
        val timeLine = TimeLine(messages)

        timeLine.sortByRecentFirst() shouldBe listOf(charliesMsg, sndBobMsg, fstBobMsg)
    }
}