package com.renansouza.folioappbackend.user;

import java.util.UUID;

record UserRecord(
        UUID uuid,
        String name,
        String email,
        String picture
) {
}