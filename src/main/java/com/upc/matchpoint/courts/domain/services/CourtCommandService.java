package com.upc.matchpoint.courts.domain.services;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.domain.model.commands.CreateCourtCommand;
import com.upc.matchpoint.courts.domain.model.commands.DeleteCourtCommand;
import com.upc.matchpoint.courts.domain.model.commands.UpdateCourtCommand;
import java.util.Optional;

public interface CourtCommandService {
    Optional<Court> handle(CreateCourtCommand command);
    Optional<Court> handle(UpdateCourtCommand command);
    void handle(DeleteCourtCommand command);
}

