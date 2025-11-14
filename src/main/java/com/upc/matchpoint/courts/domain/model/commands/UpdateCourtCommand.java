package com.upc.matchpoint.courts.domain.model.commands;

public record UpdateCourtCommand(Long courtId, String name, String location, String type) {
}

