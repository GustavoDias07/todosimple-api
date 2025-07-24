package com.lucasangelo.todosimple.services;

import com.lucasangelo.todosimple.models.Task;
import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.models.enums.ProfileEnum;
import com.lucasangelo.todosimple.repositories.TaskRepository;
import com.lucasangelo.todosimple.security.UserSpringSecurity;
import com.lucasangelo.todosimple.services.exceptions.AuthorizationException;
import com.lucasangelo.todosimple.services.exceptions.DataBindingViolationException;
import com.lucasangelo.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ServletWebServerFactoryCustomizer servletWebServerFactoryCustomizer;

    public Task findTaskById(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException
                ("Task not found! Id:" + id + ", Type: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN)
                && !userHasTask(userSpringSecurity, task)) {
            throw new AuthorizationException("Access denied");
        }

        return task;
    }

    public List<Task> findAllByUser(){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Access denied!");

        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task obj){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Access denied!");

        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setUser(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj){
        Task newObj = findTaskById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return taskRepository.save(newObj);
    }

    public void delete(long id){
        findTaskById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Error deleting task!");
        }
    }

    private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }

}

