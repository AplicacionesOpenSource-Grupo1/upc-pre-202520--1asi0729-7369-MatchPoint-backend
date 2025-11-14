package com.upc.matchpoint.courts.application.internal.queryservices;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.domain.model.queries.GetAllCourtsQuery;
import com.upc.matchpoint.courts.domain.model.queries.GetCourtByIdQuery;
import com.upc.matchpoint.courts.domain.services.CourtQueryService;
import com.upc.matchpoint.courts.infrastructure.persistence.jpa.repositories.CourtRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourtQueryServiceImpl implements CourtQueryService {
    private final CourtRepository courtRepository;

    public CourtQueryServiceImpl(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @Override
    public List<Court> handle(GetAllCourtsQuery query) {
        return courtRepository.findAll();
    }

    @Override
    public Optional<Court> handle(GetCourtByIdQuery query) {
        return courtRepository.findById(query.courtId());
    }
}

