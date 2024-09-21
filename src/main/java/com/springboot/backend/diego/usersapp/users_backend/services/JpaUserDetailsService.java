package com.springboot.backend.diego.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.diego.usersapp.users_backend.entities.User;
import com.springboot.backend.diego.usersapp.users_backend.repositories.IUserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = repository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with username %s not found", username));
        }

        User user = optionalUser.get();
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                username, 
                user.getPassword(),
                true, true, true, true, authorities);
    }
}
