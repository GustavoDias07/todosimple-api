package com.lucasangelo.todosimple.services;

import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.models.dto.UserCreateDTO;
import com.lucasangelo.todosimple.models.dto.UserUpdateDTO;
import com.lucasangelo.todosimple.models.enums.ProfileEnum;
import com.lucasangelo.todosimple.repositories.UserRepository;

import com.lucasangelo.todosimple.security.UserSpringSecurity;
import com.lucasangelo.todosimple.services.exceptions.AuthorizationException;
import com.lucasangelo.todosimple.services.exceptions.DataBindingViolationException;
import com.lucasangelo.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    public User findById(Long id){
        UserSpringSecurity userSpringSecurity = authenticated();
        if(!Objects.nonNull(userSpringSecurity) ||
                !userSpringSecurity.hasRole(ProfileEnum.ADMIN)
                        && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Access denied!");
        }

        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("User not found! Id:" + id + ", Type: " + User.class.getName())); //exeção que nao para o programa
    }

    @Transactional
    public User create(User obj){
        obj.setId(null); //garante que ta criando um novo
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Error deleting user: ");
        }
    }

    public static UserSpringSecurity authenticated(){
        try{
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj){
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj){
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }

}
