package social.network

import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

interface PersonalTimelines {

    fun publish(publishMessage: PublishMessage)
    fun view(viewTimeline: ViewTimeline) : List<Message>
}

data class Message(val value: String, val ts: LocalDateTime){

    fun getMinutesTillNow(clock: Clock): Long = ChronoUnit.MINUTES.between(ts, LocalDateTime.now(clock))
}
