package social.network.infra.repository

import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import social.network.domain.User
import social.network.domain.UserName

class InMemoryUsersRepositoryTest {

    private val users = mutableMapOf<UserName, User>()

    private val repository = InMemoryUsersRepository(users)

    @Test
    fun `should save a user`() {
        val user = User(UserName("Jane"), emptyList())

        repository.save(user)

        users shouldContain (user.userName to user)
    }

    @Test
    fun `should update a user`() {
        val user = User(UserName("Jane"), emptyList()).also(repository::save)
        val updated = user.copy(following = listOf(UserName("Jane")))

        repository.save(updated)

        users shouldContain (user.userName to updated)
    }

    @Test
    fun `should find a user`() {
        val user = User(UserName("Jane"), emptyList()).also(repository::save)

        repository.find(UserName("Jane")) shouldBe user
    }

    @Test
    fun `should not find a user when it does not exist`() {
        val user = User(UserName("Jane"), emptyList()).also(repository::save)

        repository.find(UserName("John")) shouldBe null

    }
}
