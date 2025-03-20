package org.example.authservice.service;

import org.example.authservice.model.CustomUserDetails;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j

@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                return userRepository.findByEmail(email)
                                .map((User user) -> {
                                        System.out.println(
                                                        "User Loaded by UserService " + this.getClass().getName()
                                                                        + " - " + user.getEmail());
                                        return new CustomUserDetails(user);
                                })
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Couldn't find a matching user email in the database for " + email)

                                );
        }

        public UserDetails loadUserById(Long userId) {

                return userRepository.findById(userId).map(
                                (User user) -> {
                                        System.out
                                                        .println("User Loaded by UserService "
                                                                        + this.getClass().getName() + " - "
                                                                        + user.getId());
                                        return new CustomUserDetails(user);
                                }

                ).orElseThrow(
                                () -> new UsernameNotFoundException(
                                                "Couldn't find a matching user email in the database for " + userId));

        }

}
