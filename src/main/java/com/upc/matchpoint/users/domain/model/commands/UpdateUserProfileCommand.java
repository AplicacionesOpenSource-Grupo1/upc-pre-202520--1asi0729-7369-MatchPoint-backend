package com.upc.matchpoint.users.domain.model.commands;

public record UpdateUserProfileCommand(Long userId, String name, String email, String phone) {
}

