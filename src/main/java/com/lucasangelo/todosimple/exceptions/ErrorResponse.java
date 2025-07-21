package com.lucasangelo.todosimple.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor //cria construtores para (final attributes)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationErrors> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationErrors {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if(Objects.isNull(errors)){ //se errors for vazio, cria um no array para armazenar os erros
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationErrors(field, message));//adiciona o campo/mensagem de erro
    }
}
