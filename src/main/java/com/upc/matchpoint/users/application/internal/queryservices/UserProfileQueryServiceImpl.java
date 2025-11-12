package com.upc.matchpoint.users.application.internal.queryservices;

import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import com.upc.matchpoint.users.domain.model.queries.GetAllUserProfilesQuery;
import com.upc.matchpoint.users.domain.model.queries.GetUserProfileByIdQuery;
import com.upc.matchpoint.users.domain.services.UserProfileQueryService;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileQueryServiceImpl implements UserProfileQueryService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileQueryServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<UserProfile> handle(GetAllUserProfilesQuery query) {
        return userProfileRepository.findAll();
    }

    @Override
    public Optional<UserProfile> handle(GetUserProfileByIdQuery query) {
        return userProfileRepository.findById(query.userId());
    }
}

