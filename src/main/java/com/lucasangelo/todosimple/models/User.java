package com.lucasangelo.todosimple.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucasangelo.todosimple.models.enums.ProfileEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    public static final String TABLE_NAME = "user";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id do usuario corresponde ao numero do banco de dados
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @Size( min = 2, max = 100)
    @NotBlank
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 100, nullable = false)
    @Size(min = 5, max = 100)
    @NotBlank
    private String password;

    @OneToMany(mappedBy = "user") // um usuario pode ter varias tarefas, "mapeado" pelo user
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

    @Column(name = "profile", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)//sempre busca os perfis junto com o usuario
    @CollectionTable(name = "user_profile")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(x -> ProfileEnum.toenum(x)).collect(Collectors.toSet());//tranforma os perfis em stream, os mapeia, passa o valor para x, e transforma em um set
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }

}