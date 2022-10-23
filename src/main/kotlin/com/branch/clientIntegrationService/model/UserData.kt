package com.branch.clientIntegrationService.model

import com.fasterxml.jackson.annotation.JsonAlias

data class UserData(
    @JsonAlias("login")
    val user_name: String,
    @JsonAlias("name")
    val display_name: String? = null,
    @JsonAlias("avatar_url")
    val avatar: String,
    @JsonAlias("location")
    val geo_location: String? = null,
    val email: String? = null,
    val url: String? = null,
    var created_at: String,
    val repos: MutableList<GitRepository> = mutableListOf()
    )
