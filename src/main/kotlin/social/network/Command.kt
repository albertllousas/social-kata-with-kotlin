package social.network

sealed class Command

data class PublishMessage(val user: User, val message: Message) : Command()

data class ViewTimeline(val from: User) : Command()

data class SubscribeCommand(val follower: User, val followed: User) : Command()

data class GetWallCommand(val user: User) : Command()