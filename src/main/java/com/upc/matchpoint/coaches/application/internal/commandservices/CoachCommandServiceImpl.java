package com.upc.matchpoint.coaches.application.internal.commandservices;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.commands.CreateCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.DeleteCoachCommand;
import com.upc.matchpoint.coaches.domain.model.commands.UpdateCoachCommand;
import com.upc.matchpoint.coaches.domain.services.CoachCommandService;
import com.upc.matchpoint.coaches.infrastructure.persistence.jpa.repositories.CoachRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CoachCommandServiceImpl implements CoachCommandService {
    private final CoachRepository coachRepository;

    public CoachCommandServiceImpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    public Optional<Coach> handle(CreateCoachCommand command) {
        if (coachRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Coach with name " + command.name() + " already exists");
        }
        var coach = new Coach(command.name(), command.expertise(), command.phone());
        var createdCoach = coachRepository.save(coach);
        return Optional.of(createdCoach);
    }

    @Override
    public Optional<Coach> handle(UpdateCoachCommand command) {
        return coachRepository.findById(command.coachId()).map(coachToUpdate -> {
            coachToUpdate.updateCoach(command.name(), command.expertise(), command.phone());
            return coachRepository.save(coachToUpdate);
        });
    }

    @Override
    public void handle(DeleteCoachCommand command) {
        if (!coachRepository.existsById(command.coachId())) {
            throw new IllegalArgumentException("Coach with id " + command.coachId() + " not found");
        }
        coachRepository.deleteById(command.coachId());
    }
}

