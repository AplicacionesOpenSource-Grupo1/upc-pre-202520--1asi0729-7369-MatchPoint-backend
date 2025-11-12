package com.upc.matchpoint.users.domain.services;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.domain.model.queries.GetAllUserProfilesQuery;
import com.upc.matchpoint.users.domain.model.queries.GetUserProfileByIdQuery;
import java.util.List;
import java.util.Optional;

public interface UserProfileQueryService {
    List<UserProfile> handle(GetAllUserProfilesQuery query);
    Optional<UserProfile> handle(GetUserProfileByIdQuery query);
}

