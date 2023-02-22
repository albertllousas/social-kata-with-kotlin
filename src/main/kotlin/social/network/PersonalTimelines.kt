package social.network

interface PersonalTimelines {

    fun publish(user: User, message: Message)
    fun view(user: User) : List<Message>
}
