package com.springboot.backend.diego.usersapp.users_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.diego.usersapp.users_backend.entities.Role;
import com.springboot.backend.diego.usersapp.users_backend.entities.User;
import com.springboot.backend.diego.usersapp.users_backend.models.IUser;
import com.springboot.backend.diego.usersapp.users_backend.models.UserRequest;
import com.springboot.backend.diego.usersapp.users_backend.repositories.IRolerepository;
import com.springboot.backend.diego.usersapp.users_backend.repositories.IUserRepository;

@Service
public class UserServiceImpl implements IUserService {

    private IUserRepository repository;
    private IRolerepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(IUserRepository repository, IRolerepository rolerepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = rolerepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>)this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {

        // //se podria refactorizar con un metodo para reciclaje de codigo
        // List<Role> roles = new ArrayList<>();
        // Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        // optionalRoleUser.ifPresent(roles::add);

        // if (user.isAdmin()) {
        //     Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
        //     optionalRoleAdmin.ifPresent(roles::add);
        // }
        // //

        // List<Role> roles = getRoles(user);

        user.setRoles(getRoles(user));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
        
    }

    
    @Override
    @Transactional
    public Optional<User> update(UserRequest user, Long id) {
        
        Optional<User> userOptional = repository.findById(id);
        if(userOptional.isPresent()) {
            User userUpdate = userOptional.get();
            userUpdate.setName(user.getName());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setLastname(user.getLastname());
            userUpdate.setUsername(user.getUsername());
            // userUpdate.setPassword(user.getPassword());
            
            userUpdate.setRoles(getRoles(user));
            
            repository.save(userUpdate);
            return Optional.of(userUpdate);
        } else {
            return Optional.empty();
        }
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);
    
        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
}
