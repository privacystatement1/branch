package com.branch.clientIntegrationService.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class UserNotFoundException (
    username: String
) : RuntimeException("Could not find user $username in client system")

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class RepositoryNotFoundException (
    username: String
) : RuntimeException("Could not find repositories for $username in client system")

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
open class ClientIntegrationUnavailableException (
) : RuntimeException("Error from Client Integration data source")
