package social.network.domain

interface UsersRepository {

    fun save(user: User)
    fun find(userName: UserName): User?
}

data class UserName(val value: String)

data class User(val userName: UserName, val following: List<UserName>) {
    fun follows(userName: UserName): User =
        if (!following.contains(userName)) copy(following = following + userName) else this
}
