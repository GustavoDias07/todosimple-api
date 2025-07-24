package com.lucasangelo.todosimple.controllers;

import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.models.dto.UserCreateDTO;
import com.lucasangelo.todosimple.models.dto.UserUpdateDTO;
import com.lucasangelo.todosimple.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")//search
    public ResponseEntity<User> findById(@PathVariable long id){
        User obj = this.userService.findById(id);
        return ResponseEntity.ok().body(obj);//body é onde o dado vai chegar pro front
    }

    @PostMapping//insert
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj){
        User user = this.userService.fromDTO(obj);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable long id){
        obj.setId(id);
        User user = this.userService.fromDTO(obj);
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
