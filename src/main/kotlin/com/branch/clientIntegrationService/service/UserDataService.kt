package com.branch.clientIntegrationService.service

import com.branch.clientIntegrationService.model.GitRepository
import com.branch.clientIntegrationService.model.UserData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class UserDataService(
    @Value("\${branch.client.integration.user.data.base_url}") private val userDataBaseUrl: String
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserDataService::class.java)
    }

    @Cacheable("users")
    fun getUserData(username: String): UserData? {
        val webClient: WebClient = WebClient.create()

        val userData = getUserData(username, webClient)
        val gitRepos = getUserGitRepositories(username, webClient)

        gitRepos?.apply {
            userData?.repos?.addAll(gitRepos)
        }
        return userData
    }

    private fun getUserData(
        username: String,
        webClient: WebClient
    ): UserData? {
        val userDataUrl = "$userDataBaseUrl$username"

        val userData =  webClient.get()
            .uri(userDataUrl)
            .retrieve()
            .onStatus(
                { httpStatus -> httpStatus.is4xxClientError },
                {
                    log.error("User $username not found at $userDataUrl")
                    Mono.error(UserNotFoundException(username))
                }
            )
            .onStatus(
                { httpStatus -> httpStatus.is5xxServerError },
                {
                    log.error("Client Integration Unavailable at $userDataUrl")
                    Mono.error(ClientIntegrationUnavailableException())
                }
            )
            .bodyToMono(UserData::class.java)
            .block()

            log.info("Pulled user data from GIT API for user $username")

            userData?.apply {
                created_at = created_at.replace("T", " ")
                created_at = created_at.replace("Z", "")
            }
        return userData
    }

    private fun getUserGitRepositories(
        username: String,
        webClient: WebClient
    ): List<GitRepository>? {
        val reposUrl = "$userDataBaseUrl$username/repos"
        log.info("Pulled user repository data from GIT API for user $username")

        return webClient.get()
            .uri(reposUrl)
            .retrieve()
            .onStatus(
                { httpStatus -> httpStatus.is4xxClientError },
                {
                    log.error("Repositories not found for $username at $reposUrl")
                    Mono.error(RepositoryNotFoundException(username))
                }
            )
            .onStatus(
                { httpStatus -> httpStatus.is5xxServerError },
                {
                    log.error("Client Integration Unavailable at $reposUrl")
                    Mono.error(ClientIntegrationUnavailableException())
                }
            )
            .bodyToMono(object : ParameterizedTypeReference<List<GitRepository>>() {})
            .block()
    }
}

