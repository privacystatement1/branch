package com.branch.clientIntegrationService.controller

import com.branch.clientIntegrationService.model.UserData
import com.branch.clientIntegrationService.service.UserDataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/integration")
class ClientIntegrationController(private val userDataService: UserDataService) {

    @GetMapping("/data")
    fun getUserData(@RequestParam user: String): UserData? {
        return userDataService.getUserData(user)
    }
}
