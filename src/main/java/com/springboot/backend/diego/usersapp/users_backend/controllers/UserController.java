package com.springboot.backend.diego.usersapp.users_backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.diego.usersapp.users_backend.entities.User;
import com.springboot.backend.diego.usersapp.users_backend.models.UserRequest;
import com.springboot.backend.diego.usersapp.users_backend.services.IUserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "*", originPatterns = {"*"}) // Tipico error CORS
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping
    public List<User> list() {
        return service.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<User> listPageable(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        } else {
            return ResponseEntity.status(404)
                    .body(Collections.singletonMap("error", "El usuario no se encontro por el id " + id));
        }
    }

   @PostMapping
   public ResponseEntity<User> create(@RequestBody User user) {
       User newUser = service.save(user);
       return ResponseEntity.status(201).body(service.save(newUser));
   }

   @PutMapping("/{id}")
   public ResponseEntity<User> updaEntity(@PathVariable Long id, @RequestBody UserRequest user) {
        Optional<User> userOptional = service.update(user, id);
        if(userOptional.isPresent()) {          
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<?> delete(@PathVariable Long id) {
    Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()){
            service.delete(id);;
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
   }
}
