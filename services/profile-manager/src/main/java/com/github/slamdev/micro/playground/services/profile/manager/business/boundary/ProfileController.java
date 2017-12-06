package com.github.slamdev.micro.playground.services.profile.manager.business.boundary;

import com.github.slamdev.micro.playground.java.lang.Streams;
import com.github.slamdev.micro.playground.libs.authentication.client.CurrentUser;
import com.github.slamdev.micro.playground.services.profile.manager.api.ProfileDto;
import com.github.slamdev.micro.playground.services.profile.manager.api.ProfileManagerApi;
import com.github.slamdev.micro.playground.services.profile.manager.business.control.ProfileRepository;
import com.github.slamdev.micro.playground.services.profile.manager.business.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static com.github.slamdev.micro.playground.libs.authentication.client.RoleValue.Role.ADMIN;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class ProfileController implements ProfileManagerApi {

    private final ModelMapper mapper = new ModelMapper();

    private final ProfileRepository repository;

    private final CurrentUser currentUser;

    @Override
    public ProfileDto createProfile(ProfileDto profile) {
        if (!currentUser.roles().contains(ADMIN)) {
            long userId = currentUser.id().orElseThrow(() -> new HttpClientErrorException(FORBIDDEN));
            profile.setUserId(userId);
        }
        Profile entity = mapper.map(profile, Profile.class);
        entity.setId(null);
        entity = repository.save(entity);
        return mapper.map(entity, ProfileDto.class);
    }

    @Override
    public ProfileDto deleteProfile(Long id) {
        Profile entity = getById(id);
        repository.delete(entity);
        return mapper.map(entity, ProfileDto.class);
    }

    @Override
    public ProfileDto getProfile(Long id) {
        Profile entity = getById(id);
        return mapper.map(entity, ProfileDto.class);
    }

    @Override
    public List<ProfileDto> getProfiles() {
        return Streams.iterable(repository.findAll())
                .map(entity -> mapper.map(entity, ProfileDto.class))
                .collect(toList());
    }

    @Override
    public ProfileDto updateProfile(Long id, ProfileDto profile) {
        Profile entity = getById(id);
        profile.setId(entity.getId());
        mapper.map(profile, entity);
        entity = repository.save(entity);
        return mapper.map(entity, ProfileDto.class);
    }

    private Profile getById(Long id) {
        if (currentUser.roles().contains(ADMIN)) {
            return repository.findById(id).orElseThrow(() -> new HttpClientErrorException(NOT_FOUND));
        }
        long userId = currentUser.id().orElseThrow(() -> new HttpClientErrorException(FORBIDDEN));
        return repository.findByIdAndUserId(id, userId).orElseThrow(() -> new HttpClientErrorException(NOT_FOUND));
    }
}
