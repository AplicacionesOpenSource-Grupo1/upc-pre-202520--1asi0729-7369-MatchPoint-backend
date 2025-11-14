package com.upc.matchpoint.courts.application.internal.commandservices;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.domain.model.commands.CreateCourtCommand;
import com.upc.matchpoint.courts.domain.model.commands.DeleteCourtCommand;
import com.upc.matchpoint.courts.domain.model.commands.UpdateCourtCommand;
import com.upc.matchpoint.courts.domain.services.CourtCommandService;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CourtCommandServiceImpl implements CourtCommandService {
    private final CourtRepository courtRepository;

    public CourtCommandServiceImpl(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @Override
    public Optional<Court> handle(CreateCourtCommand command) {
        if (courtRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Court with name " + command.name() + " already exists");
        }
        var court = new Court(command.name(), command.location(), command.type());
        var createdCourt = courtRepository.save(court);
        return Optional.of(createdCourt);
    }

    @Override
    public Optional<Court> handle(UpdateCourtCommand command) {
        return courtRepository.findById(command.courtId()).map(courtToUpdate -> {
            courtToUpdate.updateCourt(command.name(), command.location(), command.type());
            return courtRepository.save(courtToUpdate);
        });
    }

    @Override
    public void handle(DeleteCourtCommand command) {
        if (!courtRepository.existsById(command.courtId())) {
            throw new IllegalArgumentException("Court with id " + command.courtId() + " not found");
        }
        courtRepository.deleteById(command.courtId());
    }
}

