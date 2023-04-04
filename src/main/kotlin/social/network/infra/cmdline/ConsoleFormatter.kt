package social.network.infra.cmdline

import social.network.domain.CommandResponse
import social.network.domain.CommandResponse.MessagesView
import social.network.domain.CommandResponse.None
import social.network.domain.Message
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime

object ConsoleFormatter {

    val formatCommandResponse: (Clock) -> (CommandResponse) -> String = { clock ->
        { response ->
            when (response) {
                is MessagesView -> response.messages.joinToString("\n") { msg -> msg.format(clock) }
                None -> ""
            }
        }
    }

    private fun Message.format(clock: Clock): String {
        val duration = Duration.between(ts, LocalDateTime.now(clock))
        return when {
            duration.toMillis() < 60000 -> "${userName.value} - $value (${duration.seconds} seconds ago)"
            duration.toMillis() in 60000..119999 -> "${userName.value} - $value (1 minute ago)"
            else -> "${userName.value} - $value (${duration.toMinutes()} minutes ago)"
        }
    }
}
