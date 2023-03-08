package social.network

sealed class Command

data class PublishMessage(val user: User, val message: Message) : Command()

data class ViewTimeline(val from: User) : Command()