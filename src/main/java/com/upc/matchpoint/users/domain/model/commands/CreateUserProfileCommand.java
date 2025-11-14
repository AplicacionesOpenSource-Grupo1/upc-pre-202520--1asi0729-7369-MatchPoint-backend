package com.upc.matchpoint.users.domain.model.commands;

public record CreateUserProfileCommand(String name, String email, String phone) {
}

