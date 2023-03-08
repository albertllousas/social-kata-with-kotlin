package social.network

interface PersonalTimelines {

    fun publish(publishMessage: PublishMessage)
    fun view(viewTimeline: ViewTimeline) : List<Message>
}
