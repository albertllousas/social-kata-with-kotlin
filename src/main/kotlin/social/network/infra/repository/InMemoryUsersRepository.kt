package social.network.infra.repository

import social.network.domain.User
import social.network.domain.UserName
import social.network.domain.UsersRepository

class InMemoryUsersRepository(private val users: MutableMap<UserName, User> = mutableMapOf()) : UsersRepository {
    override fun save(user: User) {
        users[user.userName] = user
    }

    override fun find(userName: UserName): User? = users[userName]
}
