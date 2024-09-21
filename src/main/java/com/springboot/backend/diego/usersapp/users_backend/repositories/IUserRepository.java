package com.springboot.backend.diego.usersapp.users_backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
// import org.springframework.stereotype.Repository;

import com.springboot.backend.diego.usersapp.users_backend.entities.User;

// @Repository
public interface IUserRepository extends CrudRepository<User, Long> {

    //Para hacer paginacion e ir mostrando poco a poco
    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String name);
}
