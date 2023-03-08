package social.network

data class User(val name: String)

interface Users {
    fun subscribe(subscribeCommand: SubscribeCommand)
}