package social.network.infra.cmdline

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import social.network.domain.Command.PublishMessage
import social.network.domain.Command.Subscribe
import social.network.domain.Command.ViewTimeline
import social.network.domain.Command.ViewWall
import social.network.infra.cmdline.ConsoleParser.parseCommand

class ConsoleParserTest {

    @TestFactory
    fun `parse commands`() = listOf(
        Pair("Alice -> I love the weather today", PublishMessage("Alice", "I love the weather today")),
        Pair("Charlie follows Alice", Subscribe("Charlie", "Alice")),
        Pair("Alice wall", ViewWall("Alice")),
        Pair("Alice", ViewTimeline("Alice"))
    ).map { (cmdLine, cmd) ->
        dynamicTest("should parse a ${cmd::class.simpleName} command") {
            parseCommand(cmdLine) shouldBe cmd
        }
    }
}
