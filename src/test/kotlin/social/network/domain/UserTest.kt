package social.network.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `should follow another user`() {
        val john = User(UserName(value = "John"), following = emptyList())

        john.follows(UserName("Jane")) shouldBe john.copy(following = listOf(UserName("Jane")))
    }
}