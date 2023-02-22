package social.network

class SocialNetworkConsole(private val timelines: PersonalTimelines) {

    companion object{
        const val PUBLISH_DELIMITER = " -> "
    }

    fun submitCommand(command: String) {
        command
            .split(PUBLISH_DELIMITER)
            .also{ timelines.publish(User(it[0]), Message(it[1]))}
    }

}
