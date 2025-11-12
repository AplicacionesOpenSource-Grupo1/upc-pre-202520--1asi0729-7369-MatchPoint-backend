package com.upc.matchpoint.coaches.domain.services;

import com.upc.matchpoint.coaches.domain.model.aggregates.Coach;
import com.upc.matchpoint.coaches.domain.model.queries.GetAllCoachesQuery;
import com.upc.matchpoint.coaches.domain.model.queries.GetCoachByIdQuery;
import java.util.List;
import java.util.Optional;

public interface CoachQueryService {
    List<Coach> handle(GetAllCoachesQuery query);
    Optional<Coach> handle(GetCoachByIdQuery query);
}

