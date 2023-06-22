package com.renansouza.folioappbackend.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping(path = "/user/{uuid}")
    @Operation(summary = "Get one user by its uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "No user found")})
    UserRecord getOne(
            @Parameter(description = "UUID of the user to be obtained", required = true)
            @PathVariable("uuid") UUID uuid) {
        return service.findById(uuid);
    }

    @GetMapping("/user/me")
    @Operation(summary = "Get one user by its uuid")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The user",
                    content = @Content(schema = @Schema(implementation = User.class))) })
    UserRecord me(Principal principal) {
         var email = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        return service.findMe(String.valueOf(email));
    }

}