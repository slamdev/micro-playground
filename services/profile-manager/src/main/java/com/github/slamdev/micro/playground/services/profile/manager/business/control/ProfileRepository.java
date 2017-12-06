package com.github.slamdev.micro.playground.services.profile.manager.business.control;

import com.github.slamdev.micro.playground.services.profile.manager.business.entity.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findByIdAndUserId(long id, long userId);
}
