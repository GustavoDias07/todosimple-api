package com.lucasangelo.todosimple.controllers;

import com.lucasangelo.todosimple.models.Task;
import com.lucasangelo.todosimple.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")//@Get -> Indica que o mét.odo responde a requisições HTTP do tipo GET
    public ResponseEntity<Task>findById(@PathVariable long id){//@path -> Liga o valor vindo da URL ao parâmetro do métodoo
        Task obj = this.taskService.findTaskById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> findAllByUser(){
        List<Task> objs = this.taskService.findAllByUser();
        return ResponseEntity.ok().body(objs);
    }

    @PostMapping//Criar recursos (inserir no banco de dados).
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj){
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")//Atualizar recursos existentes.
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable long id ){
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")// deleta recurso no banco
    public ResponseEntity<Void> delete(@PathVariable long id){
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
    //ResponseEntity.ok().body(obj) → status 200 com um corpo.
    //
    //ResponseEntity.created(uri).build() → status 201 com URI no header.
    //
    //ResponseEntity.noContent().build() → status 204 sem corpo.




}
