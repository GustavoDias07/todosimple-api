package com.lucasangelo.todosimple.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnum {

    ADMIN(1,"ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileEnum toenum(Integer code) {

        if(Objects.isNull(code))
            return null;

        for(ProfileEnum e : ProfileEnum.values()) {//pega todos os valores
            if(code.equals(e.getCode()))
                return e;
        }

        throw new IllegalArgumentException("Invalid profile code" + code);
    }
}
