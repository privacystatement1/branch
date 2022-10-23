package com.branch.clientIntegrationService.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDataServiceTest {

    private val userDataBaseUrl: String = "https://api.github.com/users/"
    private val userDataService: UserDataService = UserDataService(userDataBaseUrl)

    @Test
    fun `get user data successful`() {
        val userData = userDataService.getUserData("octocat")
        userData?.apply {
            assertThat(user_name).isEqualTo("octocat")
            assertThat(display_name).isEqualTo("The Octocat")
            assertThat(avatar).isEqualTo("https://avatars.githubusercontent.com/u/583231?v=4")
            assertThat(geo_location).isEqualTo("San Francisco")
            assertThat(email).isNull()
            assertThat(url).isEqualTo("https://api.github.com/users/octocat")
            assertThat(created_at).isEqualTo("2011-01-25 18:44:36")
        }
    }

    @Test
    fun `get user data user not found exception`() {
        val exception = assertThrows<UserNotFoundException> {
            userDataService.getUserData("octocat8973690")
        }
        assertThat(exception.message).isEqualTo("Could not find user octocat8973690 in client system")
    }
}
