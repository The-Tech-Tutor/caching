package com.example.caching.service;

import com.example.caching.constant.CacheConstants;
import com.example.caching.model.AppUser;
import com.example.caching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public AppUser getUserByUsername(String username) {
        simulateWaitTime();
        Optional<AppUser> userOptional = userRepository.getUserByUsername(username);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /*
        https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache
    	In proxy mode (the default), only external method calls coming in through the proxy are intercepted.
    	This means that self-invocation (in effect, a method within the target object that calls another method of the target object)
    	does not lead to actual caching at runtime even if the invoked method is marked with @Cacheable. Consider using the aspectj mode in this case.
    	Also, the proxy must be fully initialized to provide the expected behavior,
    	so you should not rely on this feature in your initialization code (that is, @PostConstruct).
    */
    @Override
    @Cacheable(value = CacheConstants.DATABASE_CACHE)
    public AppUser getUserByUsernameCached(String username) {
        simulateWaitTime();
        Optional<AppUser> userOptional = userRepository.getUserByUsername(username);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AppUser saveNewUser(AppUser appUser) {
        Optional<AppUser> userOptional = userRepository.getUserByUsername(appUser.getUsername());

        if (userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        } else {
            return userRepository.save(appUser);
        }
    }

    @Override
    public AppUser updateUserStatus(String username, String status) {
        Optional<AppUser> userOptional = userRepository.getUserByUsername(username);

        if (userOptional.isPresent()) {
            AppUser appUser = userOptional.get();
            appUser.setStatus(status);
            return userRepository.save(appUser);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @CacheEvict(value = CacheConstants.DATABASE_CACHE, key = "#username")
    public AppUser updateUserStatusWithEvict(String username, String status) {
        Optional<AppUser> userOptional = userRepository.getUserByUsername(username);

        if (userOptional.isPresent()) {
            AppUser appUser = userOptional.get();
            appUser.setStatus(status);
            return userRepository.save(appUser);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private void simulateWaitTime() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
