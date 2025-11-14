package com.upc.matchpoint.courts.domain.services;

import com.upc.matchpoint.courts.domain.model.aggregates.Court;
import com.upc.matchpoint.courts.domain.model.queries.GetAllCourtsQuery;
import com.upc.matchpoint.courts.domain.model.queries.GetCourtByIdQuery;
import java.util.List;
import java.util.Optional;

public interface CourtQueryService {
    List<Court> handle(GetAllCourtsQuery query);
    Optional<Court> handle(GetCourtByIdQuery query);
}

