package de.nsctool.api.user

import de.nsctool.api.authentication.keycloak.KeycloakClient
import de.nsctool.api.authentication.keycloak.Role
import de.nsctool.api.core.exceptions.BadRequestException
import de.nsctool.api.core.exceptions.InternalServerErrorException
import de.nsctool.api.user.usercreation.UserCreationSaga
import io.kotest.assertions.throwables.shouldThrowExactly
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class UserServiceTest {
    private val client = mockk<KeycloakClient>()
    private val repository = mockk<UserRepository>()
    private val userCreationSaga = mockk<UserCreationSaga>()
    private val service = UserService(client = client, repository = repository, userCreationSaga)
    private val user = User(userName = "username", email = "f@foo.bar", id ="")
    private val password = "123456"
    private val uuid = UUID.randomUUID().toString()

    @Test
    fun `should create user`() {
        justRun { userCreationSaga.run(any()) }
        val user = User("id", "username", "email")

        service.create(user, "password")

        verify { userCreationSaga.run(match { it.user == user && it.password == "password" }) }
    }

    @Test
    fun `should handle create user error`() {
        every {
            client.createUser(
                user.email,
                user.userName,
                listOf(Role.USER)
            )
        } throws(BadRequestException("Foo"))
        shouldThrowExactly<InternalServerErrorException> { service.create(user = user, password = password) }
    }

    @Test
    fun `should handle user deletion`() {
        justRun { client.deleteUser(uuid) }
        justRun { repository.deleteById(uuid) }
        service.delete(id = uuid)

        every { client.deleteUser(uuid) } throws(BadRequestException("ex"))
        shouldThrowExactly<BadRequestException> { service.delete(uuid) }
    }
}