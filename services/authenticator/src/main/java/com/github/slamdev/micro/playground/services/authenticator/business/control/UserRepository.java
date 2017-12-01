package com.github.slamdev.micro.playground.services.authenticator.business.control;

import com.github.slamdev.micro.playground.services.authenticator.business.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Optional<User> findByEmail(String email);
}
